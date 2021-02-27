package org.fp024.j8ia.part01.chapter02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2.2 동작 파라미터화
 */
class ApplePredicateTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplePredicateTest.class);

	private final List<Apple> appleList = AppleRepository.getAppleList();

	@Test
	void test() {
		List<Apple> greenAppleResult = filterApples(appleList, new AppleGreenColorPredicate());

		assertFalse(greenAppleResult.isEmpty());
		greenAppleResult.forEach(apple -> assertEquals("green", apple.getColor()));

		List<Apple> heavyAppleResult = filterApples(appleList, new AppleHeavyWeightPredicate());

		assertFalse(heavyAppleResult.isEmpty());
		heavyAppleResult.forEach(apple -> assertTrue(apple.getWeight() >= 150));

		List<Apple> redAndHeavyAppleResult = filterApples(appleList, new AppleRedAndHeavyPredicate());
		assertFalse(redAndHeavyAppleResult.isEmpty());
		redAndHeavyAppleResult.forEach(apple -> assertEquals("red", apple.getColor()));
		redAndHeavyAppleResult.forEach(apple -> assertTrue(apple.getWeight() >= 150));

	}

	@DisplayName("2.3.2 익명 클래스 사용")
	@Test
	void testUseAnonymousClass() {
		List<Apple> redAndHeavyAppleResult = filterApples(appleList, new ApplePredicate() {
			@Override
			public boolean test(Apple apple) {
				return "red".equals(apple.getColor()) && apple.getWeight() >= 150;
			}
		});
		assertFalse(redAndHeavyAppleResult.isEmpty());
		redAndHeavyAppleResult.forEach(apple -> assertEquals("red", apple.getColor()));
		redAndHeavyAppleResult.forEach(apple -> assertTrue(apple.getWeight() >= 150));
	}

	static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
		List<Apple> result = new ArrayList<>();

		for (Apple apple : inventory) {
			if (p.test(apple)) {
				result.add(apple);
			}
		}
		return result;

	}

	/**
	 * 왜 잘 될까? ApplePredicate 에 다른 연관관계없이 평가 메서드 하나만 있는데, 잘된다.
	 */
	@DisplayName("2.3.3 람다 표현식 사용")
	@Test
	void testUseLamda() {

		List<Apple> redAndHeavyAppleResult = filterApples(appleList,
				(Apple apple) -> "red".equals(apple.getColor()) && apple.getWeight() >= 150);

		assertFalse(redAndHeavyAppleResult.isEmpty());
		redAndHeavyAppleResult.forEach(apple -> assertEquals("red", apple.getColor()));
		redAndHeavyAppleResult.forEach(apple -> assertTrue(apple.getWeight() >= 150));
	}

	/**
	 * 퀴즈 2-1 유연한 prettyPrintApple 메서드 구현
	 */
	static void prettyPrintApple(List<Apple> inventory, AppleFormatter appleFormatter) {
		for (Apple apple : inventory) {
			LOGGER.info(appleFormatter.print(apple));
		}
	}

	interface AppleFormatter {
		String print(Apple apple);
	}

	/**
	 * 단순 출력
	 */
	class AppleOnelineFormatter implements AppleFormatter {
		@Override
		public String print(Apple apple) {
			return String.format("사과색: %s, 사과무게: %dg", apple.getColor(), apple.getWeight());
		}
	}

	/**
	 * 상세 출력
	 */
	class AppleMultilineFormatter implements AppleFormatter {
		@Override
		public String print(Apple apple) {
			return String.format(" - 사과색: %s색,\n - 사과무게: %dgram \n - %s 사과", apple.getColor(), apple.getWeight(),
					(apple.getWeight() >= 150 ? "무거운" : "가벼운"));
		}
	}

	@DisplayName("퀴즈 2-1 실행")
	@Test
	void testPrettyPrintApple() {
		prettyPrintApple(appleList, new AppleOnelineFormatter());
		prettyPrintApple(appleList, new AppleMultilineFormatter());
	}

	/**
	 * p76. 퀴즈 2-2 익명 클래스 문제
	 * 
	 * ==> Runnable의 맴버를 this로 봐서 5가 될 것 같다. ==> 실행시 5로 나옴..
	 */
	class MeaningOfThis {
		public final int value = 4;

		public void doIt() {
			int value = 6;
			Runnable r = new Runnable() {
				private final Logger logger = LoggerFactory.getLogger(Runnable.class);
				public final int value = 5;

				@Override
				public void run() {
					logger.info("퀴즈 정답: {}", this.value);
				}
			};
			r.run();
		}
	}

	@DisplayName("퀴즈 2-2 실행")
	@Test
	void runQuiz2_2() {
		new MeaningOfThis().doIt();
	}

}
