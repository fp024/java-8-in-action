package org.fp024.j8ia.part02.chapter07;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 7.1 병렬 스트림
 */
@Slf4j
class ParallelStreamsTest {

	@Test
	void testSum() {
		assertEquals(iterativeSum(100), sequentialSum(100));
	}
	
	static long sequentialSum(long n) {
		return Stream
				.iterate(1L, i -> i + 1) // 무한 자연수 스트림 생성
				.limit(n) // n개 이하로 제한
				.reduce(0L, Long::sum); // 모든 숫자를 더하는 스트림 리듀싱 연산
	}
	
	static long iterativeSum(long n) {
		long result = 0;
		for(long i = 1L; i <=n; i++) {
			result += i;
		}
		return result;
	}
	
	
	/**
	 * 7.1.1 순차 스트림을 병렬 스트림으로 변환하기
	 */
	@Test
	void testTurningASequencialStreamIntoAParallelOne() {
		assertEquals(iterativeSum(100), parallelSum(100));
	}

	static long parallelSum(long n) {
		return Stream
			.iterate(1L, i -> i + 1)
			.limit(n) 
			.parallel() // 스트림을 병렬 스트림으로 변환
			.reduce(0L, Long::sum);
	}
	
	
	@Test
	void testForkJoinPoolProperty() {
		System.getProperties().forEach(( key,  value) -> {
				logger.info("key: {} / value: {}", key, value);
			}
		);
		// java.util.concurrent.ForkJoinPool.common.parallelism 프로퍼티가 미리설정되어있진 않은 것 같다.
		assertNull(System.getProperty("java.util.concurrent.ForkJoinPool.common.parallelism"));
		
		// 위에 환경 변수를 명시적으로 설정하지 않았으면... 아래와 같은 코드를 사용하여 프로세서 수를 설정한다.
		// 32스레드 시스템에서 실행했을 때... (AdoptOpenJDK 1.8.0_282)
		assertEquals(31, Runtime.getRuntime().availableProcessors() - 1);
	}
	
	/**
	 * 순차 스트림: sequentialSum: 101 msecs
	 * 전통 자바 반복: iterativeSum: 6 msecs
	 * 병렬 스트림: parallelSum: 77 msecs
	 * 
	 * 저자님은 병렬이 순차보다 느렸다고 하셨는데, 나의 경우는 병렬이 더 빠르긴하다.
	 * CPU환경이나 JDK 마이너 버전이 달라서 그럴 것 같다.
	 * 
	 * 저자님이 언급한 병렬이 느린 원인은...
	 *   1. iterate가 박싱된 객체를 생성하므로 이를 다시 언박싱하는 과정이 필요.
	 *   2. iterate가 병렬로 실행될 수 있도록 독립적인 청크로 분할하기 어려움.
	 *   
	 *   LongStream.rangeClosed 를 쓰면 쉽게 청크로 분할 할 수 있는 숫자범위 생산가능.
	 *   
	 *   
	 *  이후 LongStream.rangeClosed를 썻을 때...
	 * 	  LongStream ranged 스트림: rangedSum: 6 msecs  	   // 전통 자바 반복 방법과 성능이 같아짐.
	 *    LongStream ranged 병렬 스트림: rangedSum: 0 msecs    // 순차보다 훨씬 빨라짐 0밀리세컨트 미만으로 됨.
	 */
	@Test
	void testMeasuringStreamPerformance() {
		logger.info("순차 스트림: sequentialSum: {} msecs", PerformanceTestingUtils.measureSumPerf(ParallelStreamsTest::sequentialSum, 10_000_000));
		logger.info("전통 자바 반복: iterativeSum: {} msecs", PerformanceTestingUtils.measureSumPerf(ParallelStreamsTest::iterativeSum, 10_000_000));
		logger.info("병렬 스트림: parallelSum: {} msecs", PerformanceTestingUtils.measureSumPerf(ParallelStreamsTest::parallelSum, 10_000_000));
		logger.info("LongStream ranged 스트림: rangedSum: {} msecs", PerformanceTestingUtils.measureSumPerf(ParallelStreamsTest::rangedSum, 10_000_000));
		logger.info("LongStream ranged 병렬 스트림: rangedSum: {} msecs", PerformanceTestingUtils.measureSumPerf(ParallelStreamsTest::parallelRangedSum, 10_000_000));
	}
	
	

	
	
	static long rangedSum(long n) {
		return LongStream.rangeClosed(1, n)
				.reduce(0L, Long::sum);
	}

	
	static long parallelRangedSum(long n) {
		return LongStream.rangeClosed(1, n)
				.parallel()
				.reduce(0L, Long::sum);
	}
	
	
	/**
	 * 7.1.3 병렬 스트림의 올바른 사용법
	 * 
	 * 병렬실행의 경우 여러 스레드에서 동시에 누적자(total += value)를 실행하면서 문제 발생
	 * 결과도 정상적이지 않아짐.
	 */
	@Test
	void testUsingParallelStreamsCorrectly() {
		logger.info("사이드이펙트 합계 순차 실행: sequentialSum: {} msecs", PerformanceTestingUtils.measureSumPerf(ParallelStreamsTest::sideEffectSum, 10_000_000));
		logger.info("사이드이펙트 합계 병렬 실행: iterativeSum: {} msecs", PerformanceTestingUtils.measureSumPerf(ParallelStreamsTest::sideEffectParallelSum, 10_000_000));
	}
	
	static long sideEffectSum(long n) {
		Accumulator accumulator = new Accumulator();
		LongStream.rangeClosed(1, n)
			.forEach(accumulator::add);
		return accumulator.total;
	}
	
	
	static long sideEffectParallelSum(long n) {
		Accumulator accumulator = new Accumulator();
		LongStream.rangeClosed(1, n)
			.parallel()
			.forEach(accumulator::add);
		return accumulator.total;
	}
	
	
	/**
	 * 7.1.4 병렬 스트림을 효과적으로 사용하기 위해선..
	 * 	1. 확신이 서지 않으면 직접 측정할 것.
	 *  2. 박싱을 주의하자
	 *  3. 순차 스트림보다 병렬 스트림에서 성능이 떨어지는 연산이 있음: limit, findFirst
	 *  4. 스트림에서 수행하는 전체 파이프라인 연산 비용을 고려할 것
	 *  5. 소량의 데이터는 병렬 스트림 실행에 도움이 안됨.
	 *  6. 스트림을 구성하는 자료구조가 적절한지 확인 ArrayList가 LinkedList보다 효율적으로 분할 가능.
	 */ 
	
	
}
