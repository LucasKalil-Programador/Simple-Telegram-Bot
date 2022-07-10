package org.MyProject.ThreadPoolSys;

import static org.MyProject.ThreadPoolSys.ThreadUtils.delay;

import java.util.concurrent.TimeUnit;

/**
 * @author Lucas Guimaraes Kalil
 * 
 *         classe que gerencia a pool de threads
 */
public class BlockingThreadPool {

	/**
	 * Valor booleano que é usado para saber se o pool foi desligado ou não
	 */
	boolean shutdown = false;

	/**
	 * Array com todos os threads desse pool
	 */
	Worker[] workers;

	/**
	 * Tempo que o pool ira esperar quando não encontrar um thread livre
	 * <p>
	 * Valores menores aumentam a performance porem gasta mais cpu
	 * 
	 * @see execute
	 */
	private int poolWaitingTime_ms = 10;

	/**
	 * 
	 * Construtor padrao para o pool de threads
	 * <p>
	 * Inicia todas as threads
	 * 
	 * @param threadCount tamanho especifico do pool
	 */
	public BlockingThreadPool(int threadCount) {
		workers = new Worker[threadCount];
		for (int i = 0; i < workers.length; i++)
			workers[i] = new Worker();
	}

	/**
	 * Submete uma tarefa as threads fica esperado ate que uma das thread esteja
	 * livre
	 * 
	 * @param task tarefa a ser execultada
	 */
	public synchronized void execute(Runnable task) {
		if (shutdown) {
			System.err.println("The thread pool has been turned off");
			return;
		}
		
		int i = 0;
		while (!workers[i++].submitTask(task)) {
			if (i >= workers.length) {
				delay(poolWaitingTime_ms, TimeUnit.MILLISECONDS);
				i = 0;
			}
		}
	}

	/**
	 * Execulta o procedimento de desligamento das thread
	 * <p>
	 * os processos ainda em execulção seram concluidos.
	 */
	public synchronized void shutdown() {
		shutdown = true;
		for (Worker worker : workers)
			worker.shutdown();
	}

	/**
	 * @return poolWaitingTime_ms
	 * 
	 * @see poolWaitingTime_ms
	 */
	public int getPoolWaitingTime_ms() {
		return poolWaitingTime_ms;
	}

	/**
	 * @see poolWaitingTime_ms
	 */
	public void setPoolWaitingTime_ms(int poolWaitingTime_ms) {
		this.poolWaitingTime_ms = poolWaitingTime_ms;
	}

	/**
	 * @return threadWaitingTime_ms
	 * 
	 * @see threadWaitingTime_ms
	 */
	public int getThreadWaitingTime_ms() {
		return workers[0].getThreadWaitingTime_ms();
	}

	/**
	 * @see worker.setThreadWaitingTime_ms
	 */
	public void setThreadWaitingTime_ms(int threadWaitingTime_ms) {
		for (Worker worker : workers)
			worker.setThreadWaitingTime_ms(threadWaitingTime_ms);
	}
	
	/**
	 * @return array com os status de cada um dos threads
	 */
	public String[] getWorkersStatus() {
		String[] status = new String[workers.length];
		for (int i = 0; i < status.length; i++)
			status[i] = workers[i].getStatus();
		return status;
	}
}
