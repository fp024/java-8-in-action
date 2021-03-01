package org.fp024.j8ia.part02.chapter05;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.fp024.j8ia.part02.chapter05.Dish.Type;
import org.junit.jupiter.api.Test;

/**
 * 5.1 필터링과 슬라이싱
 * 
 */
class FilteringAndSlicingTest {
	private List<Dish> menu = DishRepository.getDishList();
	
	/**
	 * 5.1.1 프레디케이트로 필터링
	 */
	@Test
	void testFilteringWithAPredicate() {
		List<Dish> vegetarianMenu = menu.stream()
			.filter(Dish::isVegetarian)
			.collect(toList());
		
		vegetarianMenu.forEach(d -> assertTrue(d.isVegetarian()));
	}
	
	
	/**
	 * 5.1.2 고유 요소 필터링
	 */
	@Test
	void testFilteringUniqueElementsInAStream() {
		List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
		List<Integer> result = numbers.stream()
			.filter(i -> i%2==0)
			.distinct()
			.collect(toList());
		
		assertIterableEquals(Arrays.asList(2, 4), result);
	}
	
	/**
	 * 5.1.3 스트림 축소
	 */
	@Test
	void testTruncatingAStream() {
		List<Dish> heavyCalrories = menu.stream()
			.filter(d -> d.getCalories() > 300)
			.limit(3)
			.collect(toList());
		assertTrue(heavyCalrories.size() <= 3); 
		heavyCalrories.forEach(d -> assertTrue(d.getCalories() > 300));
	}


	/**
	 * 5.1.4 요소 건너뛰기
	 */
	@Test
	void testSkippingElements() {
		List<Dish> dishes = menu.stream()
				.filter(d -> d.getCalories() > 300)
				.skip(2)
				.collect(toList());

		assertIterableEquals(Arrays.asList("chicken", "french fries", "rice", "pizza", "salmon")
				, dishes.stream().map(Dish::getName).collect(toList()));
	}
	
	
	/**
	 * 퀴즈 5.1
	 * 		처음 등장하는 고기 2개 필터링
	 */
	@Test
	void testQuizFiltering() {
		List<Dish> result = menu.stream()
			.filter(d -> d.getType() == Type.MEAT)
			.limit(2)
			.collect(toList());

		assertIterableEquals(Arrays.asList("pork", "beef")
				, result.stream().map(Dish::getName).collect(toList()));
	}
}
