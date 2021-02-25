package org.fp024.j9ia.part01.chapter03;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 3.2.1 함수형 인터페이스 * 하나의 추상 메서드를 가진 인터페이스 * 전체 표현식을 함수형 인터페이스의 인터페이스로 취급할 수 있음.
 */
@Slf4j
class FunctionalInterfaceTest {
	@Test
	void test() {
		Runnable r1 = () -> logger.info("Hello World 1");

		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				logger.info("Hello World 2");
			}
		};

		// 람다를 사용한 참조
		process(r1);

		// 익명 클래스 참조 실행
		process(r2);

		// 메서드에 표현식을 직접 전달
		process(() -> logger.info("Hello World 3"));
	}

	private void process(Runnable r) {
		new Thread(r).start();
	}
}
