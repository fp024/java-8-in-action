package org.fp024.j8ia.part01.chapter03;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

/**
 * 3.6.2 생성자 레퍼런스
 */
class ConstructorReferenceTest {

	@Test
	void testSupplier() {
		Supplier<Apple> c1 = () -> new Apple();
		Supplier<Apple> c2 = Apple::new;
		assertTrue(new ReflectionEquals(c1.get()).matches(c2.get()));
	}

	@Test
	void testFunction() {
		Function<Integer, Apple> c1 = Apple::new;
		Function<Integer, Apple> c2 = (Integer i) -> new Apple(i);
		assertTrue(new ReflectionEquals(c2.apply(120)).matches(c1.apply(120)));
	}

	@Test
	void testMap() {
		List<Integer> weights = Arrays.asList(7, 3, 4, 10);
		List<Apple> actual = map(weights, Apple::new);

		List<Apple> expect = Arrays.asList(new Apple(7), new Apple(3), new Apple(4), new Apple(10));

		assertIterableEquals(expect, actual);
	}

	static List<Apple> map(List<Integer> list, Function<Integer, Apple> func) {
		List<Apple> result = new ArrayList<>();
		list.forEach(i -> result.add(func.apply(i)));
		return result;
	}

	@Test
	void testBiFunction() {
		BiFunction<String, Integer, Apple> c1 = Apple::new;
		BiFunction<String, Integer, Apple> c2 = (String s, Integer i) -> new Apple(s, i);

		assertTrue(new ReflectionEquals(c2.apply("green", 150)).matches(c1.apply("green", 150)));
	}
	
	static Map<String, Function<Integer, Fruit>> map = new HashMap<>();
	static {
		map.put("apple", Apple::new);
		map.put("orange", Orange::new);
	}

	
	static Fruit giveMeFruit(String fruit, Integer weight) {
		return map.get(fruit.toLowerCase()).apply(weight);
	}

	@DisplayName("메서드 레퍼런스를 여러 상황에 응용")
	@Test
	void testGiveMeFruit() {
		Fruit apple = giveMeFruit("Apple", 150);
		
		Fruit orange = giveMeFruit("oRaNgE", 100);
		
		assertSame(apple.getClass(), Apple.class);
		assertEquals(150, apple.getWeight());
		
		assertSame(orange.getClass(), Orange.class);
		assertEquals(100, orange.getWeight());
	}	
	

	
	
	
	
	
}
