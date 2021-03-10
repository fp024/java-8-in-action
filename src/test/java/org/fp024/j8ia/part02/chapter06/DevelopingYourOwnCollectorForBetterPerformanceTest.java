package org.fp024.j8ia.part02.chapter06;

import static java.util.stream.Collectors.partitioningBy;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 6.6 커스텀 컬렉터를 구현해서 성능 개선하기
 */
@Slf4j
class DevelopingYourOwnCollectorForBetterPerformanceTest {
	
	@Test
	void testPrimeNumbersCollector() {
		Map<Boolean, List<Integer>> actual = prititionPrimesWithCustomCollector(10);
		//  {false=[4, 6, 8, 9, 10], true=[2, 3, 5, 7]}
		logger.info(actual.toString());
	}
	
	
	Map<Boolean, List<Integer>> prititionPrimesWithCustomCollector(int n) {
		return IntStream.rangeClosed(2,  n).boxed().collect(new PrimeNumbersCollector());
	}
	
	
	@Test
	void testPrimeNumbersCollectorPerformance() {
		// 커스텀 컬랙터 버전의 E5-2667 v2 듀얼 시스템에서의 실행결과
		// Fastest execution done in 316 msecs
		performanceTest(() -> prititionPrimesWithCustomCollector(1_000_000));
	}
	
	/**
	 * 성능 테스트
	 */
	void performanceTest (Runnable runnable) {
		long fastest = Long.MAX_VALUE;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			runnable.run();
			long duration = (System.nanoTime() - start) / 1_000_000;
			if(duration < fastest) {
				fastest = duration;
			}
		}
		logger.info("Fastest execution done in {} msecs", fastest);		
	}
	

	// =============== 6.4 에서 실행 했던 코드 ===============
	// 커스텀 컬랙터 버전보다 200ms 정도 더 느림 39% 느림
	
	@Test
	void testPartitionPrimesPerformance() {
		// 6.4에서 실행했던 E5-2667 v2 듀얼 시스템에서의 실행결과
		// Fastest execution done in 516 msecs
		performanceTest(() -> partitionPrimes(1_000_000));
	}
	
	// 소수의 대상을 주어진 수의 제곱근 이하로 제한.
	boolean isPrime(int candidate) {
		int candidateRoot = (int) Math.sqrt((double)candidate);
		return IntStream.rangeClosed(2, candidateRoot)
					.noneMatch(i -> {
						// 2나 3일 때는 아예 rangeClosed가 실행되지 않아 거짓으로 판단하는 건가?
						if (candidate == 2 || candidate == 3) {
							logger.info("{} {}", candidate, i);
						}
						return candidate % i == 0;
					});
	}
	
	
	Map<Boolean, List<Integer>> partitionPrimes(int n) {
		return IntStream.rangeClosed(2,  n).boxed() /** Returns a Stream consisting of the elements of this stream, each boxed to an Integer. */
				.collect(
					partitioningBy(candidate -> isPrime(candidate)));
	}

}
