package org.fp024.j8ia.part01.chapter03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

/**
 * 3.6 메서드 레퍼런스
 * 
 * 3-5 퀴즈도 같이하자..
 */
class MetohdReferenceTest {

	@Test
	void test() {
		List<String> str = Arrays.asList("a", "b", "A", "B");

		// java.util.Comparator
		// @FunctionalInterface 어노테이션 붙어있음.
		// 구현 요구 메서드는 int compare(T o1, T o2); 하나 있다.
		str.sort((String s1, String s2) -> s1.compareToIgnoreCase(s2));
		assertEquals(Arrays.asList("a", "A", "b", "B"), str);

		str = Arrays.asList("a", "b", "A", "B");
		str.sort(String::compareToIgnoreCase); // 메서드 레퍼런스 사용
		assertEquals(Arrays.asList("a", "A", "b", "B"), str);

	}

	@Test
	void testQuiz1() {
		Function<String, Integer> stringToInteger = (String s) -> Integer.parseInt(s);
		Function<String, Integer> useMethodRef = Integer::parseInt;

		assertEquals(stringToInteger.apply("1004"), useMethodRef.apply("1004"));
	}

	@Test
	void testQuiz2() {
		BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element);
		BiPredicate<List<String>, String> containsUseMethodRef = List::contains;

		List<String> t = Arrays.asList("a", "b", "c");
		String u = "c";
		assertEquals(contains.test(t, u), containsUseMethodRef.test(t, u));
	}

}
