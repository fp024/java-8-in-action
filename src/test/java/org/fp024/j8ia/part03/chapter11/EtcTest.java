package org.fp024.j8ia.part03.chapter11;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class EtcTest {
	
	@Test
	void testStringAppend() {
		// 괄호 속 정수 연산이 먼저 수행되어 정수 7로 먼저 계산된 후.
		// 문자열 연결이 실행되어 "2+5=7"이라는 문자열이됩니다.
		assertEquals("2+5=7", "2+5=" + (2 + 5));
		// 괄호로 연산자 우선순위를 바꾸지 않았기 때문에, 왼쪽부터 문자열 연결이 실행되어 문자열 "12"가되고,
		assertEquals("12", "" + 1 + 2);
		// 정수끼리의 덧셈 연산이 되어 정수 3 입니다.
		assertEquals(3, +1 + 2);
	}


	/**
	 * Java 스터디 카페에서 asList로 만든 List가 불변이냐라는 질문이
	 * 올라와서 테스트 돌려봄.
	 */
	@Test
	void testAsList() {
		// asList로 List 생성
		List<String> list = Arrays.asList("Toy", "Box", "Robot", "Box");

		assertEquals("Toy", list.get(0));

		list.set(0, "Toy변경");

		// list의 요소의 값 자체는 변경할 수 있음.
		assertEquals("Toy변경", list.get(0));

		// 제거나, 수정메서드는 사용할 수 없음.
		assertThrows(UnsupportedOperationException.class, 
				() -> list.remove(0)
		);
		assertThrows(UnsupportedOperationException.class, 
				() -> list.add("새로운 내용 추가")
		);
	}
	
	/**
	 * 아래 영상보고 Java에 대한 테스트를 위해 벤치마크 작성
	 * 
	 * https://youtu.be/MmIxahj9vnY
	 * 
	 * Python 벤치마크
	 * 
	 * >>> import timeit
	 * >>> code = '"-".join(str(n) for n in range(100))'
	 * >>> print(timeit.timeit(code, number=10000))
	 * 0.15640659999999684
	 * 
	 * 반복의 수가 1만번일 때는 병렬수행을 해도 for-loop보다 빠르지 않지만.
	 * 
	 * 반복의 수 100만번으로 늘리고, 100만번 반복 부분을 parallel을 붙여 병렬 수행해서
	 * 병렬 스트림으로 실행시 For-Loop보다 3배 가까이 빨라진다.
	 * 
	 *   1 나노초는 1e-9 (0이 9개)
	 */
	
	@Test
	void testJoinString() {
		assertEquals("0-1-2-3-4",
				IntStream.range(0, 5)
					.mapToObj(Integer::toString)
					.collect(Collectors.joining("-"))
		);

		logger.info("joinStream: {}", benchmark(() -> joinStream(1_000_000)));
		logger.info("joinParallelStream: {}", benchmark(() -> joinParallelStream(1_000_000)));
		logger.info("joinPyConStream: {}", benchmark(() -> joinPyConStream(1_000_000)));
		logger.info("joinPyConForLoop: {}", benchmark(() -> joinPyConForLoop(1_000_000)));
	}

	double benchmark(Runnable r) {
		long start = System.nanoTime();
		r.run();
		long elapsed = System.nanoTime() - start;
		return elapsed / 1_000_000_000.0;
	}

	void joinStream(int num) {
		IntStream.range(0, num).forEach(i ->
			IntStream.range(0, 100)
				.mapToObj(Integer::toString)
				.collect(Collectors.joining("-"))
		);
	}
	
	void joinParallelStream(int num) {
		IntStream.range(0, num).parallel().forEach(i ->
			IntStream.range(0, 100)
				.mapToObj(Integer::toString)
				.collect(Collectors.joining("-"))
		);
	}
	

	void joinPyConStream(int num) {
		for (int i = 0; i < num; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j : IntStream.range(0, 100).toArray()) {
				if (sb.length() == 0) {
					sb.append(j);
				} else {
					sb.append("-").append(j);
				}
			}
		}
	}

	void joinPyConForLoop(int num) {
		for (int i = 0; i < num; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < 100; j++) {
				if (sb.length() == 0) {
					sb.append(j);
				} else {
					sb.append("-").append(j);
				}
			}
		}
	}
}
