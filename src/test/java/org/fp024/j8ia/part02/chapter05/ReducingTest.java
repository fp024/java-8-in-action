package org.fp024.j8ia.part02.chapter05;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * 5.4 리듀싱
 */
class ReducingTest {
	private List<Dish> menu = DishRepository.getDishList();
	/**
	 * 5.4.1 요소의 합
	 */
	@Test
	void testSummingTheElements() {
		List<Integer> numbers = Arrays.asList(4, 5, 3, 9);

		// 기존
		int sum = 0;
		for (Integer x : numbers) {
			sum += x;
		}

		/**
		 * BinaryOperator 
		 * T reduce(T identity, BinaryOperator<T> accumulator);
		 *                         R apply(T t, U u);
		 */
		int actual = numbers.stream().reduce(0, (a, b) -> a + b);
		assertEquals(sum, actual);
		
		// Integer의 정적 메서드로 sum이 제공되어 그것을 써도 된다.
		int actualWithIntegerSum = numbers.stream().reduce(0, Integer::sum);
		assertEquals(sum, actualWithIntegerSum);
	}
	
	/**
	 * 5.4.1 요소의 합 - 초기값 없음 
	 * 
	 * 초기값을 설정하지 않는 reduce는 Optional 객체를 반환한다.
	 */
	@Test
	void testNoInitinalValue() {
		List<Integer> emptyList = Collections.emptyList();
		Optional<Integer> actual = emptyList.stream().reduce((a, b) -> a + b);
		assertFalse(actual.isPresent());
	}

	
	/**
	 * 5.4.2 최댓값과 최소값
	 */
	@Test
	void testMaximumAndMinimum() {
		List<Integer> numbers = Arrays.asList(4, 5, 3, 9);
		Optional<Integer> max = numbers.stream().reduce(Integer::max);

		assertTrue(max.isPresent());
		assertEquals(9, max.get());
		
		Optional<Integer> min = numbers.stream().reduce(Integer::min);
		assertTrue(min.isPresent());
		assertEquals(3, min.get());
	}
	
	
	/**
	 * 5-3 퀴즈 리듀스
	 * 
	 * map과 reduce 메서드를 이용해서 스트림의 요리 개수를 계산하시오.
	 * 
	 * 잠깐 살짝 답안 컨닝을 해보니.. 제시된 메서드만으로 그냥 수를 세라는 것 같다.
	 * count()로 셀 수 있는 걸 왜이러나 했더니...ㅠㅠ
	 * 
	 * map 에 들어가는 Function 람다식이 1을 반환하게 하는것이 핵심이긴하다.
	 */
	@Test
	void testQuizReduce() {
		assertEquals(menu.size(), 
				menu.stream()
					.map(d -> 1)
					.reduce(0, Integer::sum)
		);
	}
}
