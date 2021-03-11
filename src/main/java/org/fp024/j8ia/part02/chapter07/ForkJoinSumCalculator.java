package org.fp024.j8ia.part02.chapter07;

import java.util.concurrent.RecursiveTask;

public class ForkJoinSumCalculator extends RecursiveTask<Long> {
	private static final long serialVersionUID = 4984880891305370420L;
	// 더할 숫자 배열
	private final long[] numbers;
	// 이 서브 테스크에서 처리할 배열의 초기 위치
	private final int start;
	// 이 서브 테스크에서 처리할 배열의 최종 위치
	private final int end;
	// 이 값 이하의 서브테스크는 더이상 분열 안됨.
	public static final long THRESHOLD = 10_000;

	// 메인 테스크를 생성할 때 공개 생성자
	public ForkJoinSumCalculator(long[] numbers) {
		this(numbers, 0, numbers.length);
	}

	// 메인 테스크의 서브테스크를 재귀적으로 만들 때 사용할 비공개 생성자
	// 배열의 인덱스로 사용할 때, start는 include, end는 exclude
	public ForkJoinSumCalculator(long[] numbers, int start, int end) {
		this.numbers = numbers;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Long compute() {
		// 배열의 길이
		int length = end - start;

		// 기준 값과 같거나 작으면 결과 계산
		if (length <= THRESHOLD) {
			return computeSequentially();
		}

		// 배열의 첫 번째 절반을 더하도록 서브태스크를 생성함.
		ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);

		// ForkJoinPool의 다른 스레드로 새로 생성한 태스크를 비동기로 실행함.
		leftTask.fork();

		// 배열의 나머지 절반을 더하도록 서브태스크를 생성함.
		ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);

		// 두번째 서브 태스크를 동기실행함. 이때 추가 분할이 일어날 수 있음.
		// (위에 leftTask를 fork할때도 조건이 맞다면 분할이 일어나지 않을까?)
		Long rightResult = rightTask.compute();

		// 첫번째 서브 테스크의 결과를 읽거나 아직 결과가 없으면 기다림.
		Long leftResult = leftTask.join();

		// 두 서브테스크의 결과를 조합한 값이 이 테스크의 결과임.
		return leftResult + rightResult;
	}

	// 더 분할할 수 없을 때, 서브태스크의 결과를 계산하는 단순 알고리즘
	private long computeSequentially() {
		long sum = 0;
		for (int i = start; i < end; i++) {
			sum += numbers[i];
		}
		return sum;
	}
}
