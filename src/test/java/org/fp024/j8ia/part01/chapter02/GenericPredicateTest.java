package org.fp024.j8ia.part01.chapter02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * p77. 2.3.4 리스트 형식으로 추상화
 * 
 * Predicate를 제네릭 파라미터를 적용하여 폭넓게 사용되도록 한다.
 * 
 * 이미 Java 8 에 비슷한 인터페이스가 이미 있는 것 같긴하다. java.util.function.Predicate
 * 
 */
class GenericPredicateTest {
	private final List<Apple> appleList = Arrays.asList(Apple.builder().color("green").weight(100).build(),
			Apple.builder().color("red").weight(160).build(), Apple.builder().color("green").weight(150).build(),
			Apple.builder().color("yellow").weight(80).build(), Apple.builder().color("white").weight(180).build(),
			Apple.builder().color("red").weight(140).build());

	private final List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

	interface Predicate<T> {
		boolean test(T t);
	}

	static <T> List<T> filter(List<T> inventory, Predicate<T> p) {
		List<T> result = new ArrayList<>();

		for (T t : inventory) {
			if (p.test(t)) {
				result.add(t);
			}
		}
		return result;
	}

	@Test
	void testGenericPredicate() {
		List<Apple> redAndHeavyAppleResult = filter(appleList,
				(Apple apple) -> "red".equals(apple.getColor()) && apple.getWeight() >= 150);
		assertFalse(redAndHeavyAppleResult.isEmpty());
		redAndHeavyAppleResult.forEach(apple -> assertEquals("red", apple.getColor()));
		redAndHeavyAppleResult.forEach(apple -> assertTrue(apple.getWeight() >= 150));

		List<Integer> result = filter(numbers, (Integer i) -> i % 2 == 0);
		assertEquals(Arrays.asList(2, 4, 6, 8, 10), result);
	}

}
