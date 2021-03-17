package org.fp024.j8ia.part03.chapter10;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.Test;

/**
 * 10.4 Optional 을 사용할 실용 예제
 */
class PracticalExamplesOfUsingOptionalTest {

	/**
	 * 10.4.1 잠재적으로 null이 될 수 있는 대상을 Optional로 감싸기
	 */
	@Test
	void testWrappingAPotentiallyNullValueInAnOptional() {
		Map<String, String> map = new HashMap<>();
		Optional<String> value = Optional.ofNullable(map.get("key"));
		assertFalse(value.isPresent());
	}

	/**
	 * 10.4.2 예외와 Optional
	 */
	@Test
	void testExceptionVsOptinal() {
		assertFalse(stringToInt("a").isPresent());
		assertTrue(stringToInt("1").isPresent());
		assertEquals(1, stringToInt("1").get());
	}

	static Optional<Integer> stringToInt(String s) {
		try {
			return Optional.of(Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
	
	
	/**
	 * 기본형 Optional과 이를 사용하지 말아야하는 이유
	 * 
	 *    * 어짜피 Optional은 최대 요소수가 1개일 뿐이다.
	 *    * map, flatMap을 사용할 수 없다.
	 *    * 기본형 특화 Optional로 생성환 결과는 다른 일반 Optional과 혼용할 수 없음.
	 */
	
	
	/**
	 * 10.4.3 응용
	 */
	@Test
	void testPuttingItAllTogether() {
		Properties props = new Properties();
		props.setProperty("a","5");
		props.setProperty("b", "true");
		props.setProperty("c", "-3");
		
		
		assertEquals(5, readuration(props, "a"));
		assertEquals(0, readuration(props, "b"));
		assertEquals(0, readuration(props, "c"));
		assertEquals(0, readuration(props, "d"));

			
		assertEquals(5, readurationUseOptional(props, "a"));
		assertEquals(0, readurationUseOptional(props, "b"));
		assertEquals(0, readurationUseOptional(props, "c"));
		assertEquals(0, readurationUseOptional(props, "d"));
		
		
		assertEquals(5, readurationUseOptionalBook(props, "a"));
		assertEquals(0, readurationUseOptionalBook(props, "b"));
		assertEquals(0, readurationUseOptionalBook(props, "c"));
		assertEquals(0, readurationUseOptionalBook(props, "d"));
	}
	
	
	int readuration(Properties props, String name) {
		String value = props.getProperty(name);
		if (value != null) {
			try {
				int i = Integer.parseInt(value);
				if (i > 0) {
					return i;
				}
			} catch (NumberFormatException e) {

			}
		}
		return 0;
	}	
	
	// 퀴즈 10-3 풀어보긴 했는데, 먼저 작성한 유틸리티 메서드를 사용하는 것인데, 문제를 덜읽었나보다 ㅠㅠ
	int readurationUseOptional(Properties props, String name) {
		try {
			return Optional.of(Integer.parseInt(props.getProperty(name))).filter(i -> i > 0).get();
		} catch (Exception e) {
			return 0;
		}
	}
	
	// 저자님 정답
	int readurationUseOptionalBook(Properties props, String name) {
			return Optional.ofNullable(props.getProperty(name))
				.flatMap(PracticalExamplesOfUsingOptionalTest::stringToInt)
				.filter(i -> i > 0)
				.orElse(0);
	}
	
	
	
}
