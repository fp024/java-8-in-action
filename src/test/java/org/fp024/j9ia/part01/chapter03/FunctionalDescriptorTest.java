package org.fp024.j9ia.part01.chapter03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.Callable;

import org.junit.jupiter.api.Test;

/**
 * 함수 디스크립터
 *    람다 표현식의 시그니처를 서술하는 메서드
 * 
 * 퀴즈 3-3 에서 유효한 람다 표현식 테스트
 */
class FunctionalDescriptorTest {
	@Test
	void testExecute() {
		execute(() -> {
		});
	}

	void execute(Runnable r) {
		r.run();
	}

	@Test
	void testFetch() throws Exception {
		assertEquals("Tricky example ;~)", fetch().call());
	}

	/**
	 * Callable이 T call() Exception 하나만 갖는 인터페이스이다.
	 */
	Callable<String> fetch() {
		return () -> "Tricky example ;~)";
	}

}
