package org.fp024.j8ia.part02.chapter07;

import java.util.Spliterator;
import java.util.function.Consumer;

public class WordCounterSpliterator implements Spliterator<Character> {
	private final String string;
	private int currentChar = 0;

	public WordCounterSpliterator(String string) {
		this.string = string;
	}

	// Spliterator 의 요소를 하나씩 소비하면서 탐색해야할 요소가 남아있으면 참을 반환
	@Override
	public boolean tryAdvance(Consumer<? super Character> action) {
		action.accept(string.charAt(currentChar++)); // 현재 문자 소비
		return currentChar < string.length(); // 소비할 문자가 남아있으면 true 반환
	}

	// Spliterator 의 일부요소를 분할해서 두번째 Spliterator를 생성하는 메서드
	@Override
	public Spliterator<Character> trySplit() {
		int currentSize = string.length() - currentChar;
		
		// 파싱할 문자열을 순차 처리할 수 있을 만큼 충분히 작아졌음을 알리는 null 반환
		if (currentSize < 10) {
			return null;
		}
		
		
		for (int splitPos = currentSize / 2 + currentChar;  // 파싱할 문자열의 중간을 분할 위치로 설정
			 splitPos < string.length();
			 splitPos++) {
			
			// 다음 공백이 나올 때까지 분할 위치를 뒤로 이동
			if (Character.isWhitespace(string.charAt(splitPos))) {
				Spliterator<Character> spliterator = // 처음부터 분할 위치까지 문자열을 파싱할 새로운 WordCounterSpliterator을 생성함.
						new WordCounterSpliterator(string.substring(currentChar, splitPos));
				currentChar = splitPos; // 이 WordCounterSpliterator의 시작위치를 분할 위치로 설정
				
				return spliterator;
			}			
		}
		return null;
	}

	// 탐색해야할 요소수 정보 제공
	@Override
	public long estimateSize() {
		return string.length() - currentChar;
	}

	// ORDERED: 순서고려
	// SIZED: 크기가 알려진 소스
	// SUBSIZED: 현재 Spliterator 및 분할되는 Spliterator 는 SIZED 특성을 가짐
	// NONNULL: 탐색하는 모든 요소는 NULL이아님
	// IMMUTABLE: Spliterator 의 소스는 불변
	@Override
	public int characteristics() {
		return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
	}

}
