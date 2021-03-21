package org.fp024.j8ia.part03.chapter11;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import lombok.Getter;


class Shop {
	@Getter
	private final String name;
	
	private final Random random;
	
	public Shop(String name) {
		this.name = name;
		// 상점 이름의 몇몇 문자번호의 곱을 이용해서 랜덤의 seed 값으로 사용한다.
		this.random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
	}
	
	public double getPrice(String product) {
		return calculatePrice(product);
	}
	
	private double calculatePrice(String product) {
		delay();
		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}

	private static void delay() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			/*
			 * "InterruptedException" should not be ignored (java:S2142)
			 * InterruptedExceptions should never be ignored in the code, 
			 * and simply logging the exception counts in this case as "ignoring". 
			 * 
			 * The throwing of the InterruptedException clears the interrupted state of the Thread,
			 * so if the exception is not handled properly the fact that the thread was interrupted will be lost.
			 * 
			 * Instead, InterruptedExceptions should either be rethrown - immediately or after cleaning up the method's state - 
			 * or the thread should be re-interrupted by calling Thread.interrupt() 
			 * even if this is supposed to be a single-threaded application. 
			 * 
			 * Any other course of action risks delaying thread shutdown 
			 * and loses the information that the thread was interrupted - probably without finishing its task.
			 * Similarly, the ThreadDeath exception should also be propagated. 
			 * 
			 * According to its JavaDoc:
			 * If ThreadDeath is caught by a method, it is important that it be rethrown so that the thread actually dies.
			 */
			Thread.currentThread().interrupt();
		}
	}
	
	
	// getPriceAsync 메서드 구현
	public Future<Double> getPriceAsync(String product) {
		CompletableFuture<Double> futurePrice = new CompletableFuture<>(); // 계산 결과를 포함할 CompletableFuture를 생성
		new Thread(() -> {
			
			double price = calculatePrice(product); // 다른 스레드에서 비동기 적으로 계산을 수행함.
			
			futurePrice.complete(price); // 오래 시간이 걸리는 계산이 완료되면 Future에 값을 설정한다.
			
		}).start();
		
		return futurePrice; // 계산결과가 완료되지 않길 기다리지 않고 Future 반환
	}
	
	
}
