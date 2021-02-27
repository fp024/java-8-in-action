package org.fp024.j8ia.part01.chapter03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * p117. 3.8 람다표현식을 조합할 수 있는 유용한 메서드
 * 
 */
@Slf4j
class LambdaExpressionComposeUsefulMehtodTest {
	private List<Apple> inventory;

	@BeforeEach
	void beforeEach() {
		inventory = AppleRepository.getAppleList();
	}

	/**
	 * 3.8.1 Comparator 조합 - 역정렬
	 */
	@Test
	void comparatorReverseTest() {
		Comparator<Apple> c = Comparator.comparing(Apple::getWeight);

		// 사과 무게 기준 오름차순으로 Comparator를 만들었는데.. 역순으로 하고 싶을때...
		// Comparator.reversed() 사용
		inventory.sort(c.reversed());

		assertEquals(180, inventory.get(0).getWeight());
		assertEquals(80, inventory.get(inventory.size() - 1).getWeight());
	}

	/**
	 * Comparator 연결
	 * 
	 * 무게로 사과를 비교한 다음에 무게가 같다면 원산지 국가별로 비교 정렬
	 */
	@Test
	void comparatorComposeTest() {
		inventory.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getCountry));
		inventory.forEach(i -> logger.info(i.toString()));
	}

	/**
	 * 3.8.2 Predicate 조합
	 * 
	 */
	@Test
	void predicateJoinTest() {
		Predicate<Apple> redApple = (Apple a) -> "red".equals(a.getColor());

		Predicate<Apple> notRedApple = redApple.negate();

		Predicate<Apple> notRedAndHeavyApple = notRedApple.and(a -> a.getWeight() >= 150);

		List<Apple> result = findApple(inventory, notRedAndHeavyApple);

		result.forEach(a -> {
			assertTrue((a.getWeight() >= 150) && !"red".equals(a.getColor()));
		});
	}

	static List<Apple> findApple(List<Apple> list, Predicate<Apple> p) {
		List<Apple> result = new ArrayList<>();
		for (Apple a : list) {
			if (p.test(a)) {
				result.add(a);
			}
		}
		return result;
	}

	/**
	 * 3.8.3 Function 조합
	 *   g(f(x))
	 */
	@Test
	void functionComposeAndThenMethodTest() {
		Function<Integer, Integer> f = x -> x + 1;
		Function<Integer, Integer> g = x -> x * 2;
		Function<Integer, Integer> h = f.andThen(g);

		assertEquals(4, h.apply(1));
	}

	
	/**
	 * f(g(x))
	 */
	@Test
	void functionComposeMethodTest() {
		Function<Integer, Integer> f = x -> x + 1;
		Function<Integer, Integer> g = x -> x * 2;
		Function<Integer, Integer> h = f.compose(g);
		
		assertEquals(3, h.apply(1));
	}
	
	
	@Test
	void letterClassTest() {
		Function<String, String> addHeader = Letter::addHeader;
		Function<String, String> transformationPipeline = addHeader
				.andThen(Letter::checkSpelling)
				.andThen(Letter::addFooter);
		
		assertEquals("From Raoul, Mario and Alan: I love lambda. Kind regards" , transformationPipeline.apply("I love labda."));
	}
	
}
