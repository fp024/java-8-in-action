package org.fp024.j8ia.part02.chapter07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * 7.3 Spliterator 
 * 
 * 		Spliterator : 분할할 수 있는 반복자
 */
class SpliteratorTest {
	private static final String SENTENCE = 
			"Nel   mezzo del  cammin    di   nostra   vita " +
			"mi  ritroval   in una   selva oscura" +
			" ch    la   dritta  via era   smarrita  ";
	
	
	/**
	 * 7.3.2 커스텀 Spliterator 구현하기
	 */
	@Test
	void testImplementingYourOwnSpliterator() {
		assertEquals(19, countWordsIteratively(SENTENCE));
		assertEquals(19, countWords(convertStringToCharacterStream(SENTENCE)));
		
		// 병렬 실행시 문제가 있는 상황
		assertEquals(19, countWords(convertStringToCharacterStream(SENTENCE).parallel()));
	}
	
	
	Stream<Character> convertStringToCharacterStream(String string) {
		return IntStream.range(0, string.length()).mapToObj(string::charAt);
	}
	
	
	// 7-4 반복형 단어 개수 메서드
	int countWordsIteratively(String s) {
		int counter = 0;
		boolean lastSpace = true;
		
		for (char c : s.toCharArray()) {  // 문자열의 모든 문자를 하나씩 탐색
			if (Character.isWhitespace(c)) {
				lastSpace = true;
			} else {
				if (lastSpace) { // 문자를 하나씩 탐색하다 공백 문자를 만나면
					counter++;   // 탐색한 단어로 간주하여 단어 개수 증가 시킴
				}
				lastSpace = false;
			}
		}
		return counter;
	}
	
	
	
	int countWords(Stream<Character> stream) {
		WordCounter wordCounter = stream.reduce(new WordCounter(0, true)
												,WordCounter::accumulate
												,WordCounter::combine
				);
		
		return wordCounter.getCounter();
	}
	
	
	
}
