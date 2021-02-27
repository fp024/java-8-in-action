package org.fp024.j8ia.part01.chapter02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 2.4.1 Comparator로 정렬하기
 */
@Slf4j
class ComparatorSortTest {
	private final List<Apple> appleList = AppleRepository.getAppleList();

	@Test
	void testComparator() {
		logger.info("===== 정렬 전 (Java 8 이전)=====");
		List<Apple> inventory1 = new ArrayList<>(appleList);
		inventory1.forEach(apple -> logger.info(apple.toString()));

		// 익명 클래스 구현
		inventory1.sort(new Comparator<Apple>() {
			@Override
			public int compare(Apple a1, Apple a2) {
				return a1.getWeight().compareTo(a2.getWeight());
			}
		});

		logger.info("===== 정렬 후 (Java 8 이전)=====");
		inventory1.forEach(apple -> logger.info(apple.toString()));

		logger.info("===== 정렬 전 (Java 8 에서는...)=====");
		List<Apple> inventory2 = new ArrayList<>(appleList);
		inventory2.forEach(apple -> logger.info(apple.toString()));

		// Java 8 람다 표현식
		inventory2.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

		logger.info("===== 정렬 후 (Java 8 에서는...)=====");
		inventory2.forEach(apple -> logger.info(apple.toString()));

		assertEquals(inventory1, inventory2);

	}
}
