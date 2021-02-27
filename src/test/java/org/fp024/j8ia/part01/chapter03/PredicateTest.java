package org.fp024.j8ia.part01.chapter03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

/**
 * java.util.function.Predicate<T> 를 활용
 * 
 * 저자님도 초반에 Predicate를 직접 구현해보도록 가이드 했었었다. Java 8에서 미리 제공하는 Predicate에서도 test
 * 메서드 부분은 동일하고, default 메서드가 몇개 더 있다.
 */
class PredicateTest {
	static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> result = new ArrayList<>();
		for (T t : list) {
			if (p.test(t)) {
				result.add(t);
			}
		}
		return result;
	}

	@Test
	void testPredicate() {
		List<Apple> result = filter(AppleRepository.getAppleList(),
				(Apple a) -> "red".equals(a.getColor()) && a.getWeight() < 150);
		assertFalse(result.isEmpty());

		result.forEach(a -> {
			assertEquals("red", a.getColor());
			assertTrue(a.getWeight() < 150);
		});
	}

}
