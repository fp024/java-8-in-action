package org.fp024.j8ia.part02.chapter04;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 4.3 스트림과 컬랙션
 * 
 * * 데이터를 언제 계산하느냐가 컬랙션과 스트림의 가장 큰 차이.
 * 
 * 
 * * 스트림 * 요청할 때만 요소를 계싼하는 고정된 자료구조 * 게으르게 만들어지는 컬랙션 같음.
 */
@Slf4j
class StreamsVsCollectionsTest {
	/**
	 * 4.3.1 딱 한번만 탐색할 수 있다!
	 * 
	 * assertThrows 의 람다식 안에.. 예외가 발생하지 않는 부분의 코드를 다 넣으면 SonarLint가 경고 날림, 런타임 예외
	 * 테스트 할 때.. 메서드 호출 하나만 테스트 하라고함.
	 * 
	 * 해깔릴수도 있는 부분은 ... assertThrows 의 첫번째 인자에 IllegalStateException 의 슈퍼클래스들을 넣으면
	 * 당연히 참임..
	 */
	@Test
	void traversableOnlyOnceTest() {
		List<String> title = Arrays.asList("Java8", "In", "Action");
		Stream<String> s = title.stream();
		s.forEach(logger::info);
		assertThrows(IllegalStateException.class, () -> {
			s.forEach(logger::info);
		}, "이미 소비된 스트림을 다시 탐색하려 해서 예외 발생.");
	}

	/**
	 * 4.3.2 외부 반복과 내부 반복
	 */
	@Test
	void externalVsInternalIterationTest() {
		// for-each
		List<Dish> menu = DishRepository.getDishList();
		List<String> names1 = new ArrayList<>();
		for (Dish d : menu) {
			names1.add(d.getName());
		}

		// iterator
		menu = DishRepository.getDishList();
		List<String> names2 = new ArrayList<>();

		Iterator<Dish> iterator = menu.iterator();
		while (iterator.hasNext()) {
			Dish d = iterator.next();
			names2.add(d.getName());
		}

		// stream - internal iteration
		menu = DishRepository.getDishList();
		List<String> namesOnceAssignment = new ArrayList<>();
		menu.stream().forEach(d -> namesOnceAssignment.add(d.getName())); // 메서드 블럭 내에서 한번 할당되고 재할당이 없었다면 반드시
																			// final 키워드 까지 붙이는것을 강요하진 않는다.

		List<String> names3 = menu.stream().map(Dish::getName).collect(Collectors.toList());

		assertIterableEquals(names1, names2);
		assertIterableEquals(names2, namesOnceAssignment);
		assertIterableEquals(names1, names3);
	}

}
