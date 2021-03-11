package org.fp024.j8ia.part02.chapter07;

import java.util.function.ToLongFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceTestingUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceTestingUtils.class);

	private PerformanceTestingUtils() {
		// 객체 생성방지
	}

	/**
	 * n개의 숫자를 더하는 함수의 성능 측정 메서드
	 */
	public static long measureSumPerf(ToLongFunction<Long> adder, long n) {
		long fastest = Long.MAX_VALUE;

		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			long sum = adder.applyAsLong(n);
			long duration = (System.nanoTime() - start) / 1_000_000;
			LOGGER.info("Result: {}", sum);
			if (duration < fastest) {
				fastest = duration;
			}
		}
		return fastest;
	}

	/**
	 * ForkJoinSumCalculator 테스트를 위해.. long[] numbers를 받는 메서드 만듬.
	 */
	public static long measureSumPerfUseLongArray(ToLongFunction<long[]> adder, long[] numbers) {
		long fastest = Long.MAX_VALUE;

		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			long sum = adder.applyAsLong(numbers);
			long duration = (System.nanoTime() - start) / 1_000_000;
			LOGGER.info("Result: {}", sum);
			if (duration < fastest) {
				fastest = duration;
			}
		}
		return fastest;
	}
	
}
