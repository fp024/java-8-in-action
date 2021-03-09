package org.fp024.j8ia.part02.chapter06;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

	@Override
	public Supplier<List<T>> supplier() {
		return ArrayList::new; // 수집 연산의 시발점
	}

	@Override
	public BiConsumer<List<T>, T> accumulator() {
		// return (list, item) -> list.add(item); 풀어 썼을 때...
		return List::add; // 탐색한 항목을 누적하고 바로 누적자를 고친다
	}

	@Override
	public Function<List<T>, List<T>> finisher() {
		return Function.identity(); // 항등함수 x = f(x)
	}

	/**
	 * 병렬실행시 어떻게 할지 결정되는 부분.
	 */
	@Override
	public BinaryOperator<List<T>> combiner() {
		return (list1, list2) -> {
			list1.addAll(list2); // 두번째 컨텐츠와 합쳐서 누적자 고침.
			return list1; // 변경된 첫번째 누적자 반환
		};
	}

	/**
	 * CONCURRENT: 다중 스레드에서 accumulator 를 동시 호출할 수 있음을 나타냄.
	 * IDENTITY_FINISH: 항등(恒等) 결과, 누적된 결과를 그대로 쓸 수 있다는 것을 알려주는 것 같다.
	 *                  恒 항상 항
	 *                  等 무리 등 (등에 같다는 뜻도 있음.)
	 */
	@Override
	public Set<Characteristics> characteristics() {
		return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
	}

}
