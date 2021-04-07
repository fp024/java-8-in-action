package org.fp024.j8ia.part04.chapter13;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * 기타 테스트 진행
 */
class EtcTest {
	
	@Test
	void testSplit() {		
		// 모두 빈결과라 빈 배열
		assertArrayEquals(new String[] {}, "aaaaa".split("a"));

		// 빈 결과를 생성
		// ""a""a""a""a""a"" 이런식으로 a를 구분자로 사용하여 쪼개서 6개의 빈문자가 생겼다고 보면 될 것 같음.
		assertArrayEquals(new String[] { "", "", "", "", "", "" }, "aaaaa".split("a", "aaaaa".length() + 1));

		// limit 값을 그 이상으로 주더라도 6개짜리 빈 문자열을 담은 배열임.
		assertArrayEquals(new String[] { "", "", "", "", "", "" }, "aaaaa".split("a", "aaaaa".length() + 10));
		
		// limit를 음수로 지정한다면 먼저처럼 입력문자의 수를 계산할 필요도 없이 최대로 적용됨.
		assertArrayEquals(new String[] { "", "", "", "", "", "" }, "aaaaa".split("a", -1));
		

		// b 이후의 빈결과는 생략됨, b 이전의 빈결과는 나타남.
		assertArrayEquals(new String[] { "", "", "", "", "", "b" }, "aaaaaba".split("a"));

		// limit 를 들어온 문자값만 큼 지정한다면 빈결과를 모두 포함함.
		assertArrayEquals(new String[] { "", "", "", "", "", "b", "" }, "aaaaaba".split("a", "aaaaaba".length() + 1));
		
		assertArrayEquals(new String[] { "", "", "", "", "", "b", "" }, "aaaaaba".split("a", -1));
	}
}
