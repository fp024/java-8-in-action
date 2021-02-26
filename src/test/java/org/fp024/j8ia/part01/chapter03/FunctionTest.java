package org.fp024.j8ia.part01.chapter03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

/**
 * 3.4.3 Function
 * 
 * java.util.function.Function<T, R> 인터페이스 테스트
 */
class FunctionTest {
	/**
	 * 입력된 문자열 리스트의 각 요소 길이를 리스트에 담아 반환하는 map 메서드
	 * 
	 * @param <T>  입력 타입
	 * @param <R>  결과 타입
	 * @param list 입력 데이터
	 * @param f    함수 인터페이스
	 * @return 함수로 처리된 결과 List<R>
	 */
	static <T, R> List<R> map(List<T> list, Function<T, R> f) {
		List<R> result = new ArrayList<>();

		for (T t : list) {
			result.add(f.apply(t));
		}
		return result;
	}

	@Test
	void testMap() {
		List<Integer> result = map(Arrays.asList("lambdas", "in", "action"), (String s) -> s.length());
		assertEquals(Arrays.asList(7, 2, 6), result);
	}

}
