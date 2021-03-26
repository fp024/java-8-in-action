package org.fp024.j8ia.part03.chapter11;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fp024.j8ia.part03.chapter11.ExchangeService.Money;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 11.4 비동기 작업 파이프라인 만들기
 */
@Slf4j
class PipeliningAsynchronousTasksTest {
	private final List<Shop> shops = Collections.unmodifiableList(Arrays.asList(
			new Shop("BestPrice")
		  , new Shop("LetsSaveBing")
		  , new Shop("MyFavoriteShop")
		  , new Shop("BuyItAll")		  
		  , new Shop("ShopEasy")));

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
	 * 11.4.2 할인 서비스 사용
	 */
	@Test
	void testUsingTheDiscountService() {
		// 5개 상점에 대해 가격조회, 할인율계산 각각 1초씩 걸리므로, 10초가 걸린다.
		long duration = Util.benchmark(()-> logger.info("{}", findPrices("myPhone27S")));
		logger.info("Done in {} msecs", duration);
	}
	
	List<String> findPrices(String product) {
		return shops.stream()
				.map(shop -> shop.getPriceWithDiscountCode(product))
				.map(Quote::parse)
				.map(Discount::applyDiscount)
				.collect(Collectors.toList());
	}
	
	
	/**
	 * 11.4.3 동기작업과 비동기 작업 조합하기
	 */
	@Test
	void testComposingSynchronousAndAsynchronousOperations() {
		// Done in 2038 msecs
		long duration = Util.benchmark(()-> logger.info("{}", findPricesWithCompletableFuture("myPhone27S")));
		logger.info("Done in {} msecs", duration);
	}
	
	List<String> findPricesWithCompletableFuture(String product) {
		 List<CompletableFuture<String>> priceFutures = shops.stream()
				.map(shop ->
						CompletableFuture.supplyAsync( // 각 상점에서 할인전 각격을 비동기적으로 얻음. (코드는 포함.)
							() -> shop.getPriceWithDiscountCode(product)
						, executor)
					)
				.map(future -> future.thenApply(Quote::parse))  // 상점에서 반환한 문자열을 Quote 객체로 변환
				
				.map(future -> future.thenCompose( // 결과 Future를 다른 비동기 작업과 조합해서 할인 코드를 적용.
						quote -> CompletableFuture.supplyAsync(
								() -> Discount.applyDiscount(quote)
								, executor)
							)
					)
				.collect(Collectors.toList());
		 
		 return priceFutures.stream()
				 .map(CompletableFuture::join)  // 스트림의 모든 Future가 종료되길 기다렸다 각각의 결과를 추출
				 .collect(Collectors.toList());
	}
	
	
	/**
	 * 11.4.4 독립 CompletableFuture와 비독립 CompletableFuture합치기 
	 */
	@Test
	void testCombiningTwoCompletableFuturesDependentAndIndependent() {
		// 두개의 독립된 작업을 하나로 합쳤으므로.. 1초 좀 넘을 것 같긴했음...
		// 91.040141702717
		// Done in 1020 msecs
		
		long duration = Util.benchmark(()-> {
			logger.info("{}", findFuturePriceInUSD(shops.get(0), "myPhone27S").join());
		});
		logger.info("Done in {} msecs", duration);
	}
	
	
	// 먼저는 상점 하나에 대해서만 진행했는데, 상점의 스트림으로 진행해보자. 
	@Test
	void testCombiningTwoCompletableFuturesDependentAndIndependentWithAllShops() {
		long duration = Util.benchmark(()-> {
			findFuturePriceInUSDAllShop("myPhone27S").forEach(logger::info);
		});
		logger.info("Done in {} msecs", duration);
	}

	/**
	 * 독립적인 두 개의 CompletableFuture 합치기
	 */
	CompletableFuture<Double> findFuturePriceInUSD(Shop shop, String product) {
		CompletableFuture<Double> futurePriceInUSD =
				CompletableFuture.supplyAsync(() -> shop.getPrice(product))
					.thenCombine(
							 CompletableFuture.supplyAsync(() -> ExchangeService.getRate(Money.EUR, Money.USD))
						   , (Double price, Double rate) -> { // .thenCombine의 두번째 전달 파라미터 BiFunction 부분
							   	return price * rate; 
							 }
					);		
		return futurePriceInUSD;
	}
	
	
	/**
	 * 서식을 만들어서 모든 상점에 대한 환율정보를 노출하게 해보자.
	 */
	List<String> findFuturePriceInUSDAllShop(String product) {
		
		 List<CompletableFuture<String>> priceFutures = shops.stream()
					.map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product))
							.thenCombine(
									 CompletableFuture.supplyAsync(() -> ExchangeService.getRate(Money.EUR, Money.USD))
								   , (Double price, Double rate) -> { // .thenCombine의 두번째 전달 파라미터 BiFunction 부분
									   	return String.format("%s : %.2f" , shop.getName(), (price * rate));
									 }
							)
						)
					.collect(Collectors.toList());
			 return priceFutures.stream()
					 .map(CompletableFuture::join)  
					 .collect(Collectors.toList());
	}

	@Test
	void testGetFuturePriceInUSDOnJava7() {	
		long duration = Util.benchmark(()-> {
			try {
				logger.info("{}", getFuturePriceInUSDOnJava7(shops.get(0), "myPhone27S"));
			} catch (InterruptedException | ExecutionException e) {
				logger.error(e.getMessage(), e);
				fail();
			}
		});
		logger.info("Done in {} msecs", duration);
	}

	/**
	 * 독립적인 두 개의 CompletableFuture 합치기의 Java 7 버전
	 */
	Double getFuturePriceInUSDOnJava7(Shop shop, String product) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newCachedThreadPool();
		final Future<Double> futureRate = executor.submit(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				 return ExchangeService.getRate(Money.EUR, Money.USD);
			}
		});
		
		Future<Double> futurePriceInUSD = executor.submit(new Callable<Double>() {

			@Override
			public Double call() throws Exception {
				double priceInEUR = shop.getPrice(product);
				return priceInEUR * futureRate.get();
			}
		});
		
		return futurePriceInUSD.get();
	}
	
	
	/**
	 * 11.5.1 최저가격 검색 애플리케이션 리펙토링
	 */
	@Test
	void testRefactoringTheBestPriceFinderApplication() {
		
		long duration = Util.benchmark(()-> {
			CompletableFuture<?>[] futures = findPricesWithCompletableFutureRefactoring("myPhone")
					.map(f -> f.thenAccept(r -> logger.info("{}", r))) // thenAccept 는 연산의 결과를 소비하는 Consumer를 받음.
					.toArray(size -> new CompletableFuture[size]);
				
			CompletableFuture.allOf(futures) 
				.join(); // 스트림의 모든 CompletableFuture의 실행 완료를 기다림.
			//CompletableFuture.anyOf(futures) 
			//	.join(); // 처음으로 완료한 CompletableFuture의 값으로 동작 완료.
			
		});
		logger.info("Done in {} msecs", duration);
	}
	

	/**
	 * 스트림을 메서드 호출처에서 조작하기 위해, 결과를 모아서 받지 않고, 스트림을 그대로 반환
	 */
	Stream<CompletableFuture<String>> findPricesWithCompletableFutureRefactoring(String product) {
		 return shops.stream()
				.map(shop ->
						CompletableFuture.supplyAsync(
							() -> shop.getPriceWithDiscountCode(product)
						, executor)
					)
				.map(future -> future.thenApply(Quote::parse))
				
				.map(future -> future.thenCompose( 
						quote -> CompletableFuture.supplyAsync(
								() -> Discount.applyDiscount(quote)
								, executor)
							)
					);
	}
	
	
	/**
	 * 11.5.2 응용
	 */
	@Test
	void testPuttingItToWork() {
		long start = System.nanoTime();
		CompletableFuture<?>[] futures = findPricesWithCompletableFutureRefactoring("myPhone27S")
			.map(f -> f.thenAccept(
					s -> logger.info("{} (done in {} msecs)", s, (System.nanoTime() - start) / 1_000_000)))
					.toArray(size -> new CompletableFuture[size]);
			CompletableFuture.allOf(futures) 
				.join();
		logger.info("All shops have now responded in {} msecs", (System.nanoTime() - start) / 1_000_000);
	}
	
	/**
	 * 11.6 요약
	 * 	- CompletableFuture 를 이용할 때 비동기 태스크에서 발생한 에러를 관리하고 전달 할 수 있음.
	 * 	- 동기 API를 CompletableFuture로 감싸서 비동기적으로 소비할 수 있음
	 */
	
}
