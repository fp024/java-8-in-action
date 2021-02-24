package org.fp024.j8ia.part01.chapter02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * 2.2 동작 파라미터화
 */
class ApplePredicateTest {
	private List<Apple> appleList = Arrays.asList(Apple.builder().color("green").weight(100).build(),
			Apple.builder().color("red").weight(160).build(), Apple.builder().color("green").weight(150).build(),
			Apple.builder().color("yellow").weight(80).build(), Apple.builder().color("white").weight(180).build(),
			Apple.builder().color("red").weight(140).build());

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

	static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
		List<Apple> result = new ArrayList<>();

		for (Apple apple : inventory) {
			if (p.test(apple)) {
				result.add(apple);
			}
		}
		return result;

	}

}
