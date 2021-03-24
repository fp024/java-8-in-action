package org.fp024.j8ia.part03.chapter11;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

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
	
	
	/**
	 * 11.3 비블록 코드 만들기
	 */
	@Test
	void testMakeYourCodeNonBlocking() {
		
		// 동기 코드 벤치에서는 최소 Shop 처리시간 만큼 걸린다.
		// [
		//	    BestPrice price is 123.26
		//	  , LetsSaveBing price is 169.47
		//    , MyFavoriteShop price is 214.13
		//    , BuyItAll price is 184.74
		// ]
		// 
		// Done in 4033.0 msecs
		//
		long duration = Util.benchmark(()-> logger.info("{}", findPrices("myPhone27S")));
		logger.info("Done in {} msecs", duration);
	}

	@Test
	void testMakeYourCodeNonBlockingWithParallelStream() {
		
		// 기본 제공 함수 parallelStream() 사용시
		// Done in 1029.0 msecs  수행시간이 하나의 작업시간 만큼 줄었음.
		//
		long duration = Util.benchmark(()-> logger.info("{}", findPricesWithParallelStream("myPhone27S")));
		logger.info("Done in {} msecs", duration);
	}
	

	
	private final List<Shop> shops = Collections.unmodifiableList(Arrays.asList(
			new Shop("BestPrice")
		  , new Shop("LetsSaveBing")
		  , new Shop("MyFavoriteShop")
		  , new Shop("BuyItAll")		  
		  , new Shop("ShopEasy")// 11.3.3 부터 상점 추가
		  /*
		  , new Shop("06-Shop") //  16코어 32스레드 시스템에서는 
		  , new Shop("07-Shop") //  상점을 스레드+1 만큼은 만들어야 저자님과 
		  , new Shop("08-Shop") //  동일한 환경이 만들어진다.
		  , new Shop("09-Shop") // 
		  , new Shop("10-Shop") // 
		  , new Shop("11-Shop") // 
		  , new Shop("12-Shop") // 
		  , new Shop("13-Shop") // 
		  , new Shop("14-Shop") // 
		  , new Shop("15-Shop") // 
		  , new Shop("16-Shop") // 
		  , new Shop("17-Shop") // 
		  , new Shop("18-Shop") // 
		  , new Shop("19-Shop") // 
		  , new Shop("20-Shop") // 
		  , new Shop("21-Shop") // 
		  , new Shop("22-Shop") // 
		  , new Shop("23-Shop") // 
		  , new Shop("24-Shop") // 
		  , new Shop("25-Shop") // 
		  , new Shop("26-Shop") // 
		  , new Shop("27-Shop") // 
		  , new Shop("28-Shop") // 
		  , new Shop("29-Shop") // 
		  , new Shop("30-Shop") // 
		  , new Shop("31-Shop") // 
		  , new Shop("32-Shop") // 
		  , new Shop("33-Shop") // 
		  , new Shop("34-Shop") //
		  , new Shop("35-Shop") // ""
		  */
	));
	
	
	List<String> findPrices(String product) {
		return shops.stream()
			.map(shop -> String.format("%s price is %.2f", shop.getName() ,shop.getPrice(product)))
			.collect(Collectors.toList());
	}
	

	List<String> findPricesWithParallelStream(String product) {
		return shops.parallelStream()  // 병렬 스트림으로 상점에서 가격정보를 가져옴.
			.map(shop -> String.format("%s price is %.2f", shop.getName() ,shop.getPrice(product)))
			.collect(Collectors.toList());
	}
	
	/**
	 * 11.3.2 CompletableFuture로 비동기 호출 구현하기
	 */
	@Test
	void testMakingAsynchronousRequestsWithCompletableFutures() {
		// CompletableFuture 으로 대체 구현
		// Done in 1028 msecs
		//
		// ==> parallelStream()을 사용한 것과 수행시간은 비슷하다.
		//   (참고) 책에서는 CompletableFuture 를 사용한 부분이 2초정도 걸려서 2배가까이 느리다고 나왔다. 나의 환경에서는 거의 비슷함.
		//          저자님의 컴퓨터 환경이 동시 스레드 수행 가능 수가 4개 일것 같은데..  
		// 			32스레드 동시 수행가능 시스템에서 32개 상점까지는 1초가 된다.
		//			33개 부터 2초였다.
		//
		long duration = Util.benchmark(()-> logger.info("{}", findPricesWithCompletableFuture("myPhone27S")));
		logger.info("Done in {} msecs", duration);
		
		// 같은 Shop 인스턴스의 calculatePrice() 메서드 속에 nextDoulbe 호출이 있기 때문에 첫번째 호출과 값이 달라진다.
		duration = Util.benchmark(()-> logger.info("{}", findPricesWithCompletableFutureWrongUse("myPhone27S")));
		logger.info("(하나의 스트림, 잘못된 사용) Done in {} msecs", duration);	
	}
	
	
	List<String> findPricesWithCompletableFuture(String product) {
		List<CompletableFuture<String>> priceFutures = 
				shops.stream()
					.map(shop -> CompletableFuture.supplyAsync(() -> // CompletableFuture로 각각의 가격을 비동기적으로 계산 
						String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))))
					.collect(Collectors.toList());
							
		// 새로운 스트림을 열고 스트림의 CompletableFuture들의 모든 비동기 동작이 끝나길 기다림 join 사용
		return priceFutures.stream()
				.map(CompletableFuture::join)
				.collect(Collectors.toList());

		// 스트림 연산의 게으른 특성 때문에, 하나의 파이프라인으로 연산을 하다간
		// 모든 가격정보 요청 동작이 동기화 순차적으로 이루어지는 결과가 됨.
	}
	
	
	/**
	 * 하나의 스트림으로 잘못된 사용
	 */
	List<String> findPricesWithCompletableFutureWrongUse(String product) {
		return  shops.stream()
					.map(shop -> CompletableFuture.supplyAsync(() ->  
						String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))))
					.map(CompletableFuture::join) // 비동기적으로 처리하려했어도 join을 하나의 스트림으로 처리하면 순차 동기화 동작처럼 된다.
					.collect(Collectors.toList());
	}

	
	/**
	 * 11.3.4 커스텀 Executor 사용하기
	 * 	
	 * 	Nthread = Ncpu * Ucpu * (1 + W/C)
	 * 
	 * 		Ncpu : Runtime.getRuntime().availableProcessors()가 반환하는 코어수
	 * 		Ucpu : 0 과 1 사이 값을 갖는 CPU 활용 비율
	 * 		W/C  : 대기 시간과 계산시간의 비율 
	 * 
	 *  책기준 계산대로라면   32 * 1 * ( 1 + 100 ) 이 되는 것 같은데...
	 *  스레드풀 최대 크기를 100정도로 잡으라고 한다.
	 *  
	 *  
	 *  데몬스레드:
	 *  	자바 프로그램이 종료 될 때, 강제로 실행이 종료될 수 있음.
	 */
	
	private final Executor executor = Executors.newFixedThreadPool(
			Math.min(shops.size(), 100), // 상점 수 만큼의 스레드를 갖는 풀을 생성. 
			new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setDaemon(true); // 프로그램 종료를 방해하지 않는 데몬 스레드를 사용한다.
					return t;
				}
			}
	);
	
	/**
	 * 11.3.4 커스텀 Executor 사용하기
	 * 
	 * 	스레드 풀을 사용하면 스레드풀 수 이내까지 상점이 늘어나도 1초를 조금 넘는 수준을 유지한다.
	 *  (100개 설정했으면 100깨까진 1초를 보장하는 듯)
	 * 
	 * ==> 애플리케이션의 특성이 맞는 Executor를 만들어 CompletableFuture를 활용하는 것이 올바르다.
	 */
	@Test
	void testUsingACustomExecutor() {
		long duration = Util.benchmark(()-> logger.info("{}", findPricesWithCompletableFutureWithThreadPool("myPhone27S")));
		logger.info("Done in {} msecs", duration);
	}
	
	/**
	 * CompletableFuture과 스레드 풀을 같이 사용.
	 */
	List<String> findPricesWithCompletableFutureWithThreadPool(String product) {
		List<CompletableFuture<String>> priceFutures = 
				shops.stream()
					.map(shop -> CompletableFuture.supplyAsync(() -> 
						  String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))
						, executor))
					.collect(Collectors.toList());
		return priceFutures.stream()
				.map(CompletableFuture::join)
				.collect(Collectors.toList());
	}
	
	
}


