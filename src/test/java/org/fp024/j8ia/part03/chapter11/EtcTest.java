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
	 */
	
	@Test
	void testJoinString() {
		logger.info("joinStream: {}", benchmark(() -> joinStream(10000)));
		logger.info("joinPyConStream: {}", benchmark(() -> joinPyConStream(10000)));
		logger.info("joinPyConForLoop: {}", benchmark(() -> joinPyConForLoop(10000)));
	}

	double benchmark(Runnable r) {
		long start = System.nanoTime();
		r.run();
		long elapsed = System.nanoTime() - start;
		return elapsed / 1_000_000_000.0;
	}

	void joinStream(int num) {
		IntStream.range(0, num).forEach(i ->
			IntStream.range(1, 100)
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
