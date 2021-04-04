package org.fp024.j8ia.part04.chapter13;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;

/**
 * 13.3. 재귀와 반복
 */
class RecursionVsIterationTest {

	@Test
	void test() {
		assertEquals(1 * 2 * 3 * 4 * 5, factorialIterative(5));

		assertEquals(1 * 2 * 3 * 4 * 5, factorialRecursive(5));

		assertEquals(1 * 2 * 3 * 4 * 5, factorialStream(5));

		assertEquals(1 * 2 * 3 * 4 * 5, factorialTailRecursive(5));

	}

	// 반복 방식 팩토리얼
	int factorialIterative(int n) {
		int r = 1;

		for (int i = 1; i <= n; i++) {
			r = r * i;
		}
		return r;
	}

	// 재귀 방식 팩토리얼
	long factorialRecursive(long n) {
		if (n == 1) {
			return 1;
		} else {
			return n * factorialRecursive(n - 1);
		}
	}

	// 스트림 팩토리얼
	long factorialStream(long n) {
		return LongStream.rangeClosed(1, n).reduce(1, (long a, long b) -> a * b);
	}

	// 꼬리 재귀 팩토리얼
	//   재귀 호출이 가장 마지막에 이루어지므로 꼬리재귀   
	//   factorialRecursive에서 마지막 으로 수행한 연산은 n과 재귀호출의 결과값의 곱셈
	//
	// 중간 결과를 각각의 스택 프레임으로 저장해야하는 일반 재귀와 달리 꼬리재귀에서는
	// 컴파일러가 하나의 스택 프래임을 재활용할 가능성이 생김.
	//  ( 그런데? 연산의 순서가 상관없을 때, 꼬리 재귀 적용이 가능할 것 같다.) 
	long factorialTailRecursive(long n) {
		return factorialHelper(1, n);
	}

	long factorialHelper(long acc, long n) {
		if (n == 1) {
			return acc;
		} else {
			return factorialHelper(acc * n, n - 1);
		}
		// 1, 5
		// 5, 4
		// 20, 3
		// 60, 2
		// 120, 1
		// 120
	}

}
