package org.fp024.j8ia.part02.chapter06;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import org.fp024.j8ia.part02.chapter05.Dish;
import org.fp024.j8ia.part02.chapter05.DishRepository;
import org.junit.jupiter.api.Test;

/**
 * 6.5 Collector 인터페이스
 */
class CollectorInterfaceTest {
	// 5장에서 사용한 데이터를 재사용한다.
	private final List<Dish> menu = DishRepository.getDishList();
	
	@Test
	void testToListCollector() {
		List<Dish> dishesUseToListCollector = menu.stream().collect(new ToListCollector<Dish>());
		List<Dish> dishesUseToList = menu.stream().collect(toList());
		
		assertIterableEquals(dishesUseToList, dishesUseToListCollector);
	}
	
	/**
	 * 컬렉터 구현을 만들지 않고, 커스텀 수집 수행.
	 */
	@Test
	void testWithoutCreatingACollectorImplementation() {
		List<Dish> dishesWithoutCollectorImpl = menu.stream().collect(
				  ArrayList::new  // supplier
				, List::add       // accumulator
				, List::addAll    // combiner
		);
		
		List<Dish> dishesUseToList = menu.stream().collect(toList());
		assertIterableEquals(dishesUseToList, dishesWithoutCollectorImpl);
	}

}
