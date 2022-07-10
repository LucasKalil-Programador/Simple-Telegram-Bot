package org.MyProject.ThreadPoolSys;

import static org.MyProject.ThreadPoolSys.ThreadUtils.delay;

import java.util.concurrent.TimeUnit;

public class BlockingThreadPool {
	
	boolean shutdown = false;
	Worker[] workers;
	
	public BlockingThreadPool(int threadCount) {
		workers = new Worker[threadCount];
		for (int i = 0; i < workers.length; i++) 
			workers[i] = new Worker();
	}

	/**
	 * submete uma tarefa as threads fica esperado ate que uma das thread esteja livre
	 * 
	 * @param task tarefa a ser execultada
	 */
	public synchronized void execute(Runnable task) {
		if(shutdown) {
			System.err.println("The thread pool has been turned off");
			return;
		}
		
		int i = 0;
		while(!workers[i++].submitTask(task)) {
			if(i >= workers.length) {
				delay(10, TimeUnit.MILLISECONDS);
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
	 * @return array com os status de cada um dos threads
	 */
	public String[] getWorkersStatus() {
		String[] status = new String[workers.length];
		for (int i = 0; i < status.length; i++)
			status[i] = workers[i].getStatus();
		return status;
	}
}


