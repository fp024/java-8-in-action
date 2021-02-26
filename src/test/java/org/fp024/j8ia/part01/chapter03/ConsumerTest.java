package org.fp024.j8ia.part01.chapter03;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * p97 3.4.2 Consumer
 * 
 * 제네릭 형식 T 객체를 받아서 void를 반환하는 accept라는 추상 메서드 반환
 */
@Slf4j
class ConsumerTest {

	static <T> void forEach(List<T> list, Consumer<T> c) {
		for (T i : list) {
			c.accept(i);
		}
	}

	/**
	 * 전달된 리스트를 소비하면서 뭔가를 한다?
	 */
	@Test
	void testConsumer() {
		forEach(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
				// 아래는...  Consumer의 accept 메서드를 구현하는 람다
				(Integer i) -> logger.info("{}", i));
	}

}
