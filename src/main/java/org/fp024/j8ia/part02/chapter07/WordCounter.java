package org.fp024.j8ia.part02.chapter07;

import lombok.Getter;

public class WordCounter {
	@Getter
	private final int counter;

	private final boolean lastSpace;

	public WordCounter(int counter, boolean lastSpace) {
		this.counter = counter;
		this.lastSpace = lastSpace;
	}


	// 반복 알고리즘 처럼 accumulate 메서드는 문자열의 문자를 하나씩 탐색한다. 
	// 3항연산으로 되어있었는데.. 보기쉽게 풀어쓴다.
	public WordCounter accumulate(Character c) {
		if (Character.isWhitespace(c)) {
			if (lastSpace) {
				return this;
			} else {
				return new WordCounter(counter, true);
			}
		} else {
			if (lastSpace) {
				// 문자를 하나씩 탐색하다 공백 문자를 만나면
				// 지금까지 탐색한 문자를 단어로 간주하여 단어 개수를 증가시킴. (공백문자는 제외)
				return new WordCounter(counter + 1, false);
			} else {
				return this;
			}
		}
	}

	public WordCounter combine(WordCounter wordCounter) {
		return new WordCounter(
				counter + wordCounter.counter // 두 WordCounter의 counter 값을 더함.
			  , wordCounter.lastSpace);       // counter 값만 더할 것이므로 마지막 공백은 신경쓰지 않음.  (어느 값이 들어가도 마찬가지란 말같다)
	}

}
