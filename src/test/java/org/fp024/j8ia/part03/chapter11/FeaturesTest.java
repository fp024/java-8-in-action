package org.fp024.j8ia.part03.chapter11;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 11.1 Future
 */
@Slf4j
class FeaturesTest {
	@Test
	void testFuture() {
		ExecutorService executor = Executors.newCachedThreadPool();
		
		Future<String> future = executor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return doSomeLongComputation(); // 시간이 오래 걸리는 작업은 스레드에서 비동기적으로 실행
			}
		});

		assertEquals("doSomethingElse", doSomethingElse());
		
		try {
			// 비동기 작업의 결과물을 가져옴, 결과가 준비되어있지 않으면
			// 호출 스레드가 블록 됨. 그러나 최대 1초까지만 기다리고, 
			// java.util.concurrent.TimeoutException 를 발생시키고 끝낸다.
			assertEquals("doSomeLongComputation", future.get(1, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			// 계산 중 예외 발생
			e.printStackTrace();
			fail();
		} catch (ExecutionException e) {
			// 현재 스레드에서 대기 중 인터럽트 발생
			e.printStackTrace();
			fail();
		} catch (TimeoutException e) {
			// Future 가 완료되기 전에 타임아웃 발생
			e.printStackTrace();
			fail();
		}
	}
	
	String doSomeLongComputation() {
		// 1초를 처리시간을 갖는 메서드
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		logger.info("doSomeLongComputation");
		return "doSomeLongComputation";
	}

	String doSomethingElse() {
		logger.info("doSomethingElse");
		return "doSomethingElse";
	}
	
	
	/**
	 * 11.1.1 Future 제한
	 * 
	 * Future만으로는 간결한 동시 수행코드를 구현하기 충분하지 않음.
	 * 
	 * 요구사항
	 * 		두개의 비동기 결과를 하나로 합칠 수 있어야함.
	 * 		두개의 계산 결과는 서로 독립적일 수 있다.
	 * 		비동기작업간 의존관계가 있을 수 있다.
	 * 		
	 * 		Future 집합이 실행하는 모든 테스크의 완료를 기다림.
	 * 
	 * 		Future 집합에서 가장 빨리 완료되는 테스크를 기다렸다 결과를 얻는다.
	 * 			여러 테스크가 다양한 방식으로 같은 결과를 구하는 상황
	 * 		
	 * 		프로그램 적으로 Future를 완료 시킨다.(비동기 동작에 수동으로 결과 제공)
	 * 
	 * 		Future 완료동작에 반응
	 * 			결과를 기다리면서 블록되지 않고, 
	 * 			결과가 준비되는 대로 Future의 결과르 원하는 추가기능 동작 수행가능.
	 * 
	 */
	
	
	
	/**
	 * 11.2 비동기 API 구현
	 * 11.2.1 동기 메서드를 비동기 메서드로 반환
	 * 11.2.2 에러 처리방법
	 */
	@Test
	void testConvertingASynchronousMehtodIntoAnAsynchronousOne() {
		
		Shop shop = new Shop("BestShop");
		
		long start = System.nanoTime();
		
		Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
		
		long invocationTime = ((System.nanoTime() - start) / 1_000_000);
		
		logger.info("Invocation returned after {} msecs", invocationTime);
		
		doSomethingElse();
		
		try {
			double price = futurePrice.get();
			logger.info(String.format("Price is %.2f%n", price));
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		
		long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
		
		logger.info("Price returned after {} msecs", retrievalTime);
	}
}
