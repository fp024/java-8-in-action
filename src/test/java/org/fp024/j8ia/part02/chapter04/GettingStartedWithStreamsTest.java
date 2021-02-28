package org.fp024.j8ia.part02.chapter04;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 4.2 스트림 시작하기
 */
@Slf4j
class GettingStartedWithStreamsTest {
	private List<Dish> menu = DishRepository.getDishList();

	@Test
	void internalIterationTest() {
		List<String> threeHighCaloricDishNames = menu.stream()
				.filter(d -> d.getCalories() > 300)
				.map(Dish::getName)
				.limit(3) // 선착순 3개만 선택
				.collect(Collectors.toList());

		logger.info("{}", threeHighCaloricDishNames);	
	}
}
