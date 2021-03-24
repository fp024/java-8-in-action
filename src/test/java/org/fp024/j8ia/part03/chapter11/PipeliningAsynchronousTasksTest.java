package org.fp024.j8ia.part03.chapter11;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

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
	
	
	
	
	
	
}
