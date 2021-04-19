package org.fp024.j8ia.part04.chapter14;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 14.3 스트림과 게으른 평가
 */
@Slf4j
class LazyEvaluationWithStreamsTest {

	/**
	 * 14.3.1 자기 정의 스트림
	 */
	@Test
	void testPrimes() {
		List<Integer> primesList = primes(5).collect(Collectors.toList());
		logger.info("{}", primesList);

		assertEquals(Arrays.asList(2, 3, 5, 7, 11), primesList);

	}

	static Stream<Integer> primes(int n) {
		return Stream.iterate(2, i -> i + 1).filter(LazyEvaluationWithStreamsTest::isPrime).limit(n);
	}

	static boolean isPrime(int candidate) {
		int candidateRoot = (int) Math.sqrt((double) candidate);
		return IntStream.rangeClosed(2, candidateRoot).noneMatch(i -> candidate % i == 0);
	}

	/**
	 * 1. 소수를 선택할 숫자 스트림이 필요
	 */
	static IntStream numbers() {
		return IntStream.iterate(2, n -> n + 1);
	}

	/**
	 * 스트림에서 첫번째 수를 가져옴 (스트림의 머리(head)) 이 숫자는 소수 (처음에 이숫자는 2)
	 */
	static int head(IntStream numbers) {
		return numbers.findFirst().getAsInt();
	}

	/**
	 * 3. 꼬리 필터링 스트림의 꼬리(tail) 에서 가져온 수로 나누어 떨어지는 모든 수를 걸러 제외
	 */
	/*
	 * 꼬리 얻기 TODO: 그런데 이게 왜 꼬리지 그냥 한칸만 무시하고 넘어가는 것인데?
	 */
	static IntStream tail(IntStream numbers) {
		return numbers.skip(1);
	}

	@Test
	void testTail() {
		List<Integer> result = tail(IntStream.rangeClosed(1, 3)).boxed().collect(Collectors.toList());
		logger.info("tail {}", result);
		assertEquals(Arrays.asList(2, 3), result);
	}

	@Test
	void testTailFiltering() {
		IntStream numbers = numbers();
		int head = head(numbers); // 전달받은 스트림에 최종 연산을 사용하게 되기 때문에... 재상용할 수 없는데... 재사용을 계속 시도하고 있다.
		IntStream filtered = tail(numbers).filter(n -> n % head != 0);
	}

	/**
	 * 4. 재귀적으로 소수 스트림 생성
	 */
	static IntStream primesFirstImproving(IntStream numbers) {
		int head = head(numbers);
		return IntStream.concat(IntStream.of(head), primesFirstImproving(tail(numbers).filter(n -> n % head != 0)));
	}

	/**
	 * 나쁜 소식
	 */
	@Test
	void testPrimesFirstImproving() {
		// 최종 연산을 사용한 부분이 있어서, 예외가 발생한다.
		// 발생 예외: java.lang.IllegalStateException: stream has already been operated upon
		// or closed
		assertThrows(IllegalStateException.class, () -> primesFirstImproving(IntStream.rangeClosed(2, 10)));
	}

	
	/**
	 * 게으른 평가
	 * 
	 * 	
	 * 	 * TODO: 스칼라 코드가 예시로 나왔는데, 이부분은 이해가 안된다.
	 * 		def numbers(n: Int): Stream[Int] = n #:: nubmers(n + 1)
	 * 
	 * 		def primes(numbers: Stream[Int]): Stream[Int] = {
	 * 			number.head #:: primes(numbers.tail filter (n 0> n % numbers.head != 0))
	 *      }
	 */
	
}
