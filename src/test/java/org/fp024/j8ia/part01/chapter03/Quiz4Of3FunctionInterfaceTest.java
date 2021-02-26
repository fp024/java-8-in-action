package org.fp024.j8ia.part01.chapter03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 퀴즈 3-4 함수형 인터 페이스 처음 생각했을 대... 좀 어려운 문제같았음.
 */
@Slf4j
class Quiz4Of3FunctionInterfaceTest {
	/*
	 * 1. T-> R ==> Function<T, R>
	 */
	@Test
	void testFunction() {
		Function<String, Integer> func = (String s) -> s.length();
		assertEquals(10, func.apply("1234567890"));
	}

	/**
	 * 2. (int, int) -> int
	 * 
	 * 정수 두개 받아서 연산결과를 뽑아 내는 것 같은데.. , 그런데 기본타입을 썼으므로 기본형 특화 인터페이스를 쓴다.
	 * 
	 * ==> IntBinaryOperator
	 * 
	 */
	@Test
	void testIntBinaryOperator() {
		IntBinaryOperator func = (int i, int j) -> i * j;
		assertEquals(200, func.applyAsInt(10, 20));
	}

	/**
	 * 3. T -> void
	 * 
	 * 소비만해서 리턴이 없는건가?
	 * 
	 * ==> Consumer<T>
	 * 
	 */
	@Test
	void testConsumer() {
		Consumer<String> func = (String s) -> logger.info("{}", s);
		func.accept("소비해서 출력만함...");
	}

	/**
	 * 4. () -> T
	 * 
	 * Supplier<T> 파라미터 요구사항이 없고 그냥 get()하나 있음.
	 * 
	 */
	@Test
	void testSupplier() {
		Supplier<String> stringSupplier = () -> "result";
		assertEquals("result", stringSupplier.get());
	}

	/**
	 * 5. (T, U) -> R
	 * 
	 * BiFunction<T, U, R> 그냥 Function<T, R> 과는 다르게 함수 인자를 2가지 타입으로 받는다.
	 */
	@Test
	void testBiFunction() {
		BiFunction<String, Integer, String> func =  (String s, Integer i) -> s + i;
		assertEquals("정수:1", func.apply("정수:", 1));
	}
	
	
	
	
	
	
	
	
	
}
