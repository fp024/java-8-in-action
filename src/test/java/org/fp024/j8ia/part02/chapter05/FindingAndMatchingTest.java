package org.fp024.j8ia.part02.chapter05;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 5.3 검색과 매칭
 * 
 * 
 */
@Slf4j
class FindingAndMatchingTest {
	private List<Dish> menu = DishRepository.getDishList();

	/**
	 * 5.3.1 프레디케이트가 적어도 한 요소와 일치하는지 확인
	 */
	@Test
	void testCheckingToSeeIfAPredicateMatchesAtLeastOneElement() {
		// anyMatch는 최종 연산
		assertTrue(menu.stream().anyMatch(Dish::isVegetarian));

		// 스트림을 탐색해서 조건에 맞는게 발견이 되면 바로 끝낼것 같다.
		// 책에서 금방말하는데 Short-circuiting evaluation 라고 한다.
	}
	
	/**
	 * 5.3.2 프레디케이트가 모든 요소와 일치하는지 검사
	 */
	@Test
	void testCheckingToSeeIfAPredicateMatchesAllElements() {
		// allMatch
		assertTrue(menu.stream().allMatch(d -> d.getCalories() < 1000));
		
		// noneMatch
		assertTrue(menu.stream().noneMatch(d -> d.getCalories() >= 1000));
	}
	
	/**
	 * 5.3.3 요소 검색 
	 */
	@Test
	void testFindingElements() {
		Optional<Dish> dish = menu.stream()
			.filter(Dish::isVegetarian)
			.findAny();
		
		assertTrue(dish.isPresent());
		assertTrue(dish.get().isVegetarian());
		
		menu.stream()
			.filter(Dish::isVegetarian)
			.findAny()
			.ifPresent(d-> logger.info(d.toString()));
	}

	
	/**
	 * 5.3.4 첫 번째 요소 찾기
	 * 
	 * 병렬실행시는 첫번째 요소를 찾기 힘들기 때문에, 
	 * 병렬실행시는 findAny를 사용한다.
	 */
	@Test
	void testFindingTheFirstElement() {
		List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
		Optional<Integer> firstSquareDivisiableByThree = 
				someNumbers.stream()
					.map(i -> i * i)
					.filter(i -> i % 3 == 0)
					.findFirst();
		assertEquals(9, firstSquareDivisiableByThree.get());
	}
}
