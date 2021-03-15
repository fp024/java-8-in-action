package org.fp024.j8ia.part03.chapter08;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 8.4 디버깅, 8.5 요약
 * 
 */
@Slf4j
class DebugingTest {
	/**
	 * java.lang.NullPointerException
  	 * at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
	 */
	@Test
	void testLambdasAndStackTraces() {
		assertThrows(NullPointerException.class, () -> {
			try {
				List<Point> points = Arrays.asList(new Point(12, 2), null);
				points.stream().map(Point::getX).forEach(System.out::println);
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
				throw e;
			}
		});
	}
	
	
	/**
		java.lang.ArithmeticException: / by zero
			at org.fp024.j8ia.part03.chapter08.DebugingTest.divideByZero(DebugingTest.java:50) ~[test-classes/:?]
			at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193) ~[?:1.8.0_282]
	 */
	@Test
	void testMethodRef() {
		assertThrows(ArithmeticException.class, () -> {
			try {
				List<Integer> numbers = Arrays.asList(1, 2, 3);
				numbers.stream().map(DebugingTest::divideByZero).forEach(i -> logger.info("{}", i));
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
				throw e;
			}
		});
	}

	static int divideByZero(int n) {
		return n / 0;
	}
	

	/**
	 * 8.4.2 정보 로깅
	 *   peek은 스트림의 각요소를 소비한 것 처럼 동작을 실행.. 그러나 실제로 소비하지 않음.
	 */
	@Test
	void testLoggingInfomation() {
		List<Integer> numbers = Arrays.asList(2,3,4,5);
		
		List<Integer> result = numbers.stream()
			.peek(x -> logger.info("from stream: {}", x))
			.map(x -> x + 17)
			.peek(x -> logger.info("after map: {}", x))
			.filter(x -> x % 2 == 0)
			.peek(x -> logger.info("after filter: {}", x))
			.limit(3)
			.peek(x -> logger.info("after limit: {}", x))
			.collect(Collectors.toList());

		assertEquals(Arrays.asList(20, 22), result);
	}
	
	
	
	/**
	 * 8.5 요약
	 * 
	 * 	람다 표현식 자체를 테스트 하는 것보다는 람다 표현식이 사용되는 메서드의 동작을 테스트하는 것이 바람직함.
	 * 
	 */
}
