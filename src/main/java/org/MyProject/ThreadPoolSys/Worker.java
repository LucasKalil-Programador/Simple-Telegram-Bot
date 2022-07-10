package org.MyProject.ThreadPoolSys;

import static org.MyProject.ThreadPoolSys.ThreadUtils.delay;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker extends Thread {

	/**
	 * Contagem estatica da quantidade de threads
	 */
	private static AtomicInteger threadCounter = new AtomicInteger(0);

	private boolean active = true;
	private Runnable task;
	private String status;

	public Worker() {
		super("Worker: " + threadCounter.getAndIncrement());
		status = getName() + " Status: idle";
		start();
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
				delay(100, TimeUnit.MILLISECONDS);
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
		if (this.task == null && active) {
			this.task = task;
			return true;
		}
		return false;
	}

	/**
	 * Desativa essa thread
	 */
	public void shutdown() {
		active = false;
	}

	/**
	 * @return status atual do thread
	 */
	public String getStatus() {
		return status;
	}
}
