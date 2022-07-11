package org.MyProject.ThreadPoolSys;

import static org.MyProject.ThreadPoolSys.ThreadUtils.delay;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author Lucas Guimaraes Kalil
 * 
 *         classe representa um trabalhado ou thread onde e possivel submeter
 *         tarefas
 *
 * @see Thread
 */
public class Worker extends Thread {

	/**
	 * Contagem estatica da quantidade de threads
	 */
	private static AtomicInteger threadCounter = new AtomicInteger(0);

	/**
	 * Tempo que o thread ira esperar quando não tiver uma tarefa sendo execultada
	 * <p>
	 * Valores menores aumentam a performance porem gastam mais cpu
	 */
	private int threadWaitingTime_ms = 10;

	private boolean active = true;
	private Runnable task;
	private String status;

	public Worker() {
		super("Worker: " + threadCounter.getAndIncrement());
		status = getName() + " Status: idle";
	}

	/**
	 * Tarefa interna da thread execulta as tarefas agendadas
	 * <p>
	 * obs. não deve ser chamada fora dessa thread
	 */
	@Override
	public void run() {
		setDefaultUncaughtExceptionHandler((t, e) -> e.printStackTrace());
		while (active) {
			if (task != null) {
				status = getName() + " Status: working";
				task.run();
				status = getName() + " Status: idle";
				task = null;
			} else {
				delay(threadWaitingTime_ms, TimeUnit.MILLISECONDS);
			}
		}
		status = getName() + " Status: off";
	}

	/**
	 * Submete uma tarefa ao thread
	 * <p>
	 * A tarefa sera aceita caso não exista uma tarefa na fila e essa thread esteja
	 * ativa
	 * 
	 * @param task tarefa a ser execultada
	 * 
	 * @return true tarefa aceita, false tarefa reculsada
	 * 
	 */
	public boolean submitTask(Runnable task) {
		if(!started) start();
		if (this.task == null && active) {
			this.task = task;
			return true;
		}
		return false;
	}
	
	/**
	 * armazena se o thread foi iniciado ou não
	 * 
	 * @see start
	 */
	private boolean started = false;
	
	@Override
	public synchronized void start() {
		started = true;
		super.start();
		System.out.println("iniciado");
	}

	/**
	 * Desativa essa thread
	 */
	public void shutdown() {
		active = false;
	}

	/**
	 * @return threadWaitingTime_ms
	 * 
	 * @see threadWaitingTime_ms
	 */
	public int getThreadWaitingTime_ms() {
		return threadWaitingTime_ms;
	}

	/**
	 * @see threadWaitingTime_ms
	 */
	public void setThreadWaitingTime_ms(int threadWaitingTime_ms) {
		this.threadWaitingTime_ms = threadWaitingTime_ms;
	}

	/**
	 * @return status atual do thread
	 */
	public String getStatus() {
		return status;
	}
}
