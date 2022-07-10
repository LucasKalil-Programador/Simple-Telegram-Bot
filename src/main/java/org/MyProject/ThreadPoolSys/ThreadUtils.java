package org.MyProject.ThreadPoolSys;

import java.util.concurrent.TimeUnit;

public final class ThreadUtils {
	
	/**
	 * Faz a thread esperar por um tempo determinado minimo de 1 milisecond
	 * 
	 * @param time tempo que o delay levara
	 * 
	 * @param unit escala de tempo
	 * 
	 * @throws IllegalArgumentException caso o time seja menor que 0
	 * 
	 * @throws NullPointerException caso a unidade seja nula
	 */
	public static void delay(long time, TimeUnit unit) {
		try {
			Thread.sleep(unit.toMillis(time));
		} catch (IllegalArgumentException | NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) { }
	}
}
