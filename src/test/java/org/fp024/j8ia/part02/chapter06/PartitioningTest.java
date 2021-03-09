package org.fp024.j8ia.part02.chapter06;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.fp024.j8ia.part02.chapter05.Dish;
import org.fp024.j8ia.part02.chapter05.Dish.Type;
import org.fp024.j8ia.part02.chapter05.DishRepository;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 6.4 분할
 */
@Slf4j
class PartitioningTest {
	// 5장에서 사용한 데이터를 재사용한다.
	private final List<Dish> menu = DishRepository.getDishList();
	
	@Test
	void testPartitioning() {
		Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian));
		
		// {false=[pork, beef, chicken, prawns, salmon], true=[french fries, rice, season fruit, pizza]}
		logger.info(partitionedMenu.toString());
		
		partitionedMenu.get(true).forEach(dish -> assertTrue(dish.isVegetarian()));
		
		assertIterableEquals(menu.stream().filter(Dish::isVegetarian).collect(toList())
				, partitionedMenu.get(true));
	}

	/**
	 * 6.4.1 분할의 장점
	 * 
	 * 	참, 거짓 두가지 요소의 스트림 리스트를 모두 유지하는 것이 분할의 장점.
	 */
	@Test
	void testAdvantagesOfPartitioning() {
		
		Map<Boolean, Map<Type, List<Dish>>> vegetarianDishesByType = menu.stream().collect(partitioningBy(Dish::isVegetarian /** 분할함수 */
					, groupingBy(Dish::getType))); /** 두번째 컬랙터 */
		
		// {false={FISH=[prawns, salmon], MEAT=[pork, beef, chicken]}, true={OTHER=[french fries, rice, season fruit, pizza]}}
		logger.info(vegetarianDishesByType.toString());
		
		// 채식 요리와 채식이 아닌요리 각각의 그룹에서 칼로리가 가장 높은 음식
		Map<Boolean, Dish> mostCaloricPartitionedByVegetarian = menu.stream().collect(partitioningBy(Dish::isVegetarian,   /**  a predicate used for classifying input elements */
									collectingAndThen(  /**  a Collector implementing the downstream reduction */
										  maxBy(comparingInt(Dish::getCalories))  /**  a collector */
										, Optional::get /** a function to be applied to the final result of the downstream collector */
									)
							)
				);
		
		// {false=pork, true=pizza}
		logger.info(mostCaloricPartitionedByVegetarian.toString());
		
	}
	
	
	/**
	 * 퀴즈 6.2 partitioningBy 사용
	 */
	@Test
	void testUsingPartitioningBy() {
		// 1.
		/*
			{false={false=[chicken, prawns, salmon], true=[pork, beef]}
			, true={false=[rice, season fruit], true=[french fries, pizza]}
		 */
		Map<Boolean, Map<Boolean, List<Dish>>> quiz1 = menu.stream().collect(partitioningBy(Dish::isVegetarian, partitioningBy(d -> d.getCalories()> 500)));
		logger.info(quiz1.toString());
		

		// 2. 컴파일 에러... partitionBy의 인자로 사용되는 Predicate의 test 메서드의 반환은 boolean형식이여야함.
		// menu.stream().collect(partitioningBy(Dish::isVegetarian, partitioningBy(Dish::getType)));
		
		// 3.
		/*
		 	{false=5, true=4}
		 */
		Map<Boolean, Long> quiz3 = menu.stream().collect(partitioningBy(Dish::isVegetarian, counting()));
		logger.info(quiz3.toString());
	}
	
	
	/**
	 * 6.4.2 숫자를 소수와 비소수로 분할하기
	 */
	@Test
	void testPartitioningNumbersIntoPrimeAndNonprime() {
		Map<Boolean, List<Integer>> actual = partitionPrimes(10);
		Map<Boolean, List<Integer>> expect = new HashMap<>();
		
		expect.put(true, Arrays.asList(2, 3, 5, 7));
		expect.put(false, Arrays.asList(4, 6, 8, 9, 10));

		assertEquals(expect, actual);
		

		
	}
	
	
	boolean isPrimeOld(int candidate) {
		return IntStream.range(2, candidate)
					.noneMatch(i -> candidate % i == 0);
	}
	
	
	// 소수의 대상을 주어진 수의 제곱근 이하로 제한.
	boolean isPrime(int candidate) {
		int candidateRoot = (int) Math.sqrt((double)candidate);
		return IntStream.rangeClosed(2, candidateRoot)
					.noneMatch(i -> {
						// 2나 3일 때는 아예 rangeClosed가 실행되지 않아 거짓으로 판단하는 건가?
						if (candidate == 2 || candidate == 3) {
							logger.info("{} {}", candidate, i);
						}
						return candidate % i == 0;
					});
	}
	
	
	Map<Boolean, List<Integer>> partitionPrimes(int n) {
		return IntStream.rangeClosed(2,  n).boxed() /** Returns a Stream consisting of the elements of this stream, each boxed to an Integer. */
				.collect(
					partitioningBy(candidate -> isPrime(candidate)));
	}
	
}
