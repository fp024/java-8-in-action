package org.fp024.j8ia.part03.chapter11;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * 책에는 언급이 안되는데, Mario Fusco 님의 예제 소스보고 옮김.
 * 
 * https://github.com/java8/Java8InAction/blob/master/src/main/java/lambdasinaction/chap11/ExchangeService.java
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ExchangeService {
	@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
	enum Money {
		USD(1.0), EUR(1.35387), GBP(1.69715), CAD(.92106), MXN(.07683);
		
		@Getter
		private final double rate;
	}

	static double getRate(Money source, Money destination) {
		return getRateWithDelay(source, destination);
	}

	private static double getRateWithDelay(Money source, Money destination) {
		Util.delay();
		return destination.rate / source.rate;
	}
	
}
