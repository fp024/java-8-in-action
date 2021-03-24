package org.fp024.j8ia.part03.chapter11;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

class Shop {
	@Getter
	private final String name;
	
	private final Random random;
	
	Shop(String name) {
		this.name = name;
		// 상점 이름의 몇몇 문자번호의 곱을 이용해서 랜덤의 seed 값으로 사용한다.
		this.random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2) * 1L);
	}
	
	double getPrice(String product) {
		return calculatePrice(product);
	}
	
	// 354쪽 부터 getPrice 메서드 구현이 할인 로직을 적용해서 String으로 반환하는데...
	// 따로 클래스로 분리하긴 애매해서, 메서드를 새로만든다
	// 아직까진 가격에 할인율을 적용하진 않았고 코드만 포함한다.
	//
	// 반환값==> 상점이름:할인적용안된가격:할인코드
	//
	String getPriceWithDiscountCode(String product) {
		double price = this.getPrice(product);
		Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
		
		return String.format("%s:%.2f:%s", name, price, code);
	}
	
	private double calculatePrice(String product) {
		Util.delay();
		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}

	// getPriceAsync 메서드 구현
	Future<Double> getPriceAsync(String product) {
		/*
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();
		new Thread(() -> {
			try {
				double price = ; 
				futurePrice.complete(price);
			} catch (Exception ex) {
				futurePrice.completeExceptionally(ex);
			}
		}).start();
		
		return futurePrice;
		*/
		// 펙토리 메서드 supplyAsync로 CompletableFuture 만들기
		return CompletableFuture.supplyAsync(() -> calculatePrice(product));
	}
}



/**
 * 할인 코드
 */
class Discount {
	enum Code {
		NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

		private final int percentage;

		Code (int percentage) {
			this.percentage = percentage;
		}
	
	}
	
	/** 기존 가격에 할인율 적용 */
	static String applyDiscount(Quote quote) {
		return quote.getShopName() + " price is " + 
				Discount.apply(quote.getPrice(), quote.getDiscountCode());
	}
	
	/** 할인율을 적용할 때, 지연 에뮬레이션 */
	private static double apply(double price, Code code) {
		Util.delay();
		return Util.format(price * (100 - code.percentage) / 100);
	}
	
}


/**
 * 상점에서 검색한 문자열 파싱
 */
@Getter
@RequiredArgsConstructor
class Quote {
	private final String shopName;
	private final double price;
	private final Discount.Code discountCode;
	
	public static Quote parse(String s) {
		String[] split = s.split(":");
		String shopName = split[0];
		double price = Double.parseDouble(split[1]);
		Discount.Code discountCode = Discount.Code.valueOf(split[2]);
		return new Quote(shopName, price, discountCode);
	}
}


/**
 * format은 저자님 제공한 코드를 봤을 때, 별도 Util 클래스로 분리되어있었다. 일단 관련 부분만 뽑아온다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Util {
	private static final DecimalFormat formatter = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.KOREA));
	
	static double format(double number) {
		synchronized (formatter) {  // DecimalFormat 에 대해 synchronized 처리가 설정됨. 
	            return new Double(formatter.format(number));
        }
	} 
	
	static void delay() {
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
			// Thread.currentThread().interrupt();
			throw new IllegalStateException(e);
		}
	}
	
	// 마이크로초 이하 소수점은 절삭
	static long benchmark(Runnable r) {
		long start = System.nanoTime();
		r.run();
		return (System.nanoTime() - start) / 1_000_000;
	}
	
}





