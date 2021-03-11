package org.fp024.j8ia.part02.chapter07;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 7.2 포크 / 조인 프레임워크
 */
@Slf4j
class ForkJoinFrameworkTest {
	/**
	 * 7.2.1 RecursiveTask 활용 스레드 풀 이용시 RecursiveTask<R> 의 서브 클래스를 만들어야함.
	 */
	@Test
	void testRecursiveTask() {
		// 포크/조인 프레임워크 활용 합계: 45 msecs
		// 병렬 스트림을 이용할 때보다 느림., 저자님 말로는 FockJoinSumCalculator 테스크에서 사용할 수 있도록 전체 스트림을
		// long[]으로 변환해서 그렇다고함.
		logger.info("포크/조인 프레임워크 활용 합계: {} msecs",
				PerformanceTestingUtils.measureSumPerf(ForkJoinFrameworkTest::forkJoinSum, 10_000_000));

		// 초기 배열 생성을 벤치마크 외부로 뺏을 때, 실행시간이 많이 줄어듬.
		// 포크/조인 프레임워크 활용 합계(배열생성부분을 벤치마크 외부로...): 8 msecs
		long[] numbers = LongStream.rangeClosed(1, 10_000_000).toArray();
		logger.info("포크/조인 프레임워크 활용 합계(배열생성부분을 벤치마크 외부로...): {} msecs", 
				PerformanceTestingUtils.measureSumPerfUseLongArray(ForkJoinFrameworkTest::forkJoinSumWithoutToArray, numbers));
	}

	static long forkJoinSum(long n) {
		long[] numbers = LongStream.rangeClosed(1, n).toArray();
		ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
		return new ForkJoinPool().invoke(task);
	}

	/**
	 * 초기에 LongStream 을 toArray하는 부분을 외부로 뺌
	 */
	static long forkJoinSumWithoutToArray(long[] numbers) {
		ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
		return new ForkJoinPool().invoke(task);
	}

	
	
	/**
	 * 7.2.2 포크/조인 프레임워크를 제대로 사용하는 방법
	 * 	- join 메서드를 테스크에 호출하면 생산하는 결과가 준비될 때 까지 호출자를 블록 시킨다.
	 *    => 두 서브 테스크가 모두 시작 된 다음에 join을 호출해야함.
	 * 
	 * - RecursiveTask 내에서는 ForkJoinPool의 invoke 메서드를 사용하지 말아야함.
	 * 
	 * - 서브 테스크에 fork 메서드를 호출하여 ForkJoinPool의 일정을 조절할 수 있음.
	 *    왼쪽과 오른쪽 모두에 fork를 호출하는 것이 자연스러울 것 같지만.. 한쪽 작업에는 fork보단 compute를 실행하는 것이 나음
	 *    두 서브 테스크의 한 테스크에서는 같은 스레드를 재사용할 수 있으므로 풀에서 불필요한 테스크를 할당하는 오버헤드를 피할 수 있음.
	 *    
	 * - 포크/조인 프레임워크를 이용하는 병렬계산은 디버깅이 어려움.   
	 */
	
	
	/**
	 * 7.2.3 작업 훔치기
	 * 
	 *   할일이 없어진 스레드가 유휴상태로 바뀌지 않고, 다른 스레드 큐의 꼬리에서 작업을 훔쳐옴.
	 * 
	 */
	
	
}