package org.fp024.j8ia.part02.chapter05;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * 5.6 숫자형 스트림
 */
class NumericStreamsTest {
	private List<Dish> menu = DishRepository.getDishList();

	/**
	 * 5.6.1 기본형 특화 스트림 - 숫자 스트림으로 매핑 
	 */
	@Test
	void testPrimitiveStreamSpecializations() {
		// 내부적으로 합계를 계산하기 전에 Integer를 기본형으로 Unboxing해야한다.
		Integer boxingCost = menu.stream()
			.map(Dish::getCalories)  // Stream<Integer>
			.reduce(0, Integer::sum); // public static int sum(int a, int b) : sum 계산을 위해 unboxing
		
		 /*
		  menu.stream()
			.map(Dish::getCalories) // .map이 Stream<Integer> 를 반환하여, sum을 사용할 수 없다.
			.sum();
		*/
		
		
		// 숫자 스트림으로 매핑
		int usePrimitiveStream = menu.stream()
				.mapToInt(Dish::getCalories) // 기본형 특화 스트림중 하나인 IntStream 을 반환하는 함수 사용 
				.sum(); // IntStream 인터페이스가 제공하는 sum 사용
		
		assertEquals(boxingCost, usePrimitiveStream);
	
		
		// 객체 스트림으로 복원하기
		IntStream intStream = menu.stream()
			.mapToInt(Dish::getCalories);
		
		Stream<Integer> integerStream = intStream.boxed(); // 기본형 int 스트림을 Integer 스트림으로 변환
	
		
		// 기본값 : OptinalInt
		OptionalInt maxCalories = menu.stream()
			.mapToInt(Dish::getCalories)
			.max();
		
		// 원래 주어진 데이터가 없어서 제공할 값이 없는 경우를 고려해서, OptionalXxx 제공
		assertEquals(800, maxCalories.getAsInt());
		
		
		// 주어진 내용이 없어서 최대 값이 없을 때의 orElse() 의 사용
		int result = Arrays.stream(new int[] {}).max().orElse(-1);
		assertEquals(-1, result);
	}
	
	
	/**
	 * 5.6.2 숫자 범위
	 * 	rangeClosed 는 마지막 값 포함
	 * 	range 는 마지막 값 미포함.
	 */
	@Test
	void testNumericRanges() {
		assertEquals(50, IntStream.rangeClosed(1, 100).filter(n -> n % 2 == 0).count());
	}
	
}
