package org.fp024.j8ia.part01.chapter03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * p115. 3.7 람다, 메서드 레퍼런스 활용하기!
 * 
 * 목표: inventory.sort(comparing(Apple::getWeight))
 */
class LambdaMethodRefUseTest {

	static class AppleComparator implements Comparator<Apple> {
		@Override
		public int compare(Apple a1, Apple a2) {
			return a1.getWeight().compareTo(a2.getWeight());
		}
	}

	/**
	 * 3.7.1 코드 전달
	 */
	@Test
	void level1PassCodeTest() {
		List<Apple> inventory = AppleRepository.getAppleList();
		inventory.sort(new AppleComparator());
		assertEquals(80, inventory.get(0).getWeight());
	}

	/**
	 * 3.7.2 익명 클래스 사용
	 */
	@Test
	void level2UseAnomoymus() {
		List<Apple> inventory = AppleRepository.getAppleList();
		inventory.sort(new Comparator<Apple>() {
			@Override
			public int compare(Apple a1, Apple a2) {
				return a1.getWeight().compareTo(a2.getWeight());
			}
		});
		assertEquals(180, inventory.get(inventory.size() - 1).getWeight());
	}

	/**
	 * 3.7.3 람다 표현식 사용
	 */
	@Test
	void level3UseLambdaExpression() {
		List<Apple> inventory = AppleRepository.getAppleList();
		
		inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));
		
		assertEquals(80, inventory.get(0).getWeight());
		assertEquals(180, inventory.get(inventory.size() - 1).getWeight());
	}
	
	
	@Test
	void level3UseLambdaExpressionAndComparing() {
		List<Apple> inventory = AppleRepository.getAppleList();
		
		inventory.sort(Comparator.comparing((Apple a) -> a.getWeight()));
		
		assertEquals(80, inventory.get(0).getWeight());
		assertEquals(180, inventory.get(inventory.size() - 1).getWeight());
	}
	
	/*
	 * "사용된 comparing 정적 메서드"
	 * 
	 * T는 비교할 엘리먼트 이고, 정렬키의 타입(getWeight의 리턴 타입이 Integer이므로 추론된것 같음.)
	 	public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
	            Function<? super T, ? extends U> keyExtractor)
	    {
	        Objects.requireNonNull(keyExtractor);
	        return (Comparator<T> & Serializable)
	            (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
	    }
	 */


	/**
	 * 3.7.4 메서드 레퍼런스 사용
	 */
	@Test
	void level4UseMethodReference() {
		List<Apple> inventory = AppleRepository.getAppleList();

		inventory.sort(Comparator.comparing(Apple::getWeight));
		
		assertEquals(80, inventory.get(0).getWeight());
		assertEquals(180, inventory.get(inventory.size() - 1).getWeight());
	}

}
