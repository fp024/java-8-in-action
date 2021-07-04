package org.fp024.j8ia.part04.chapter14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

class JustSort {
	public void sort(List<?> list) {
		Collections.reverse(list);
	}
}

class ArrangeList3 {
	static List<Integer> run() {
		List<Integer> ls = Arrays.asList(1, 3, 5, 7, 9);

		JustSort js = new JustSort(); // js는 사실상 final

		Consumer<List<Integer>> c = e -> js.sort(e);
		c.accept(ls);
		
		return ls;
	}
}

class ArrangeList4 {
	static List<Integer> run() {
		List<Integer> ls = Arrays.asList(1, 3, 5, 7, 9);

		Consumer<List<Integer>> c = e -> {
			JustSort js = new JustSort();
			js.sort(e);
		};
		c.accept(ls);
		return ls;
	}
}

/**
 * 익명 클래스를 포함하는 메서드의 매개변수나 외부변수가 final이어야 하는 이유
 * https://okky.kr/article/418106
 */
class ArrangeListAnonymous {
	
	
	static List<Integer> run() {
		List<Integer> ls = Arrays.asList(1, 3, 5, 7, 9);

		// Java 8 미만 이였다면  js에 반드시 final을 붙여서 참조변수 불변이 되도록 명시 해줘야했음.
		JustSort js = new JustSort();

		Consumer<List<Integer>> c = new Consumer<List<Integer>>() {
			@Override
			public void accept(List<Integer> e) {
				js.sort(e);
			}
		};
		
		// Local variable js defined in an enclosing scope must be final or effectively final
		// js = null;
		
		c.accept(ls);
		return ls;
	}
}

class LambdaTest {
	@Test
	void test() {
		assertEquals(Arrays.asList(9, 7, 5, 3, 1), ArrangeList3.run());
		assertEquals(Arrays.asList(9, 7, 5, 3, 1), ArrangeList4.run());
		assertEquals(Arrays.asList(9, 7, 5, 3, 1), ArrangeListAnonymous.run());
	}
}
