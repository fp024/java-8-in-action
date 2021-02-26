package org.fp024.j8ia.part01.chapter03;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

/**
 * 함수형 인터페이스에서 제네릭을 쓰는데,
 * 
 * 기본형에 대한 박싱이 일어날때, 비용이 소모되므로,
 * 
 * 오토박싱을 피할 수 있는 특별한 버전의 함수형 인터페이스도 제공함.
 *
 * * IntPredicate, LongPredicate, DoublePredicate 
 * * IntConsumber, LongConsumer, DoubleConsumer ....
 * 
 * 그런데 당연하게도 IntPredicate의 내부 메서드 정의를 보면 인터페이스 파라미터가 그냥 기본형이고 제네릭을 사용하는 부분이 없으므로
 * 오토박싱이 일어나지 않는 것은 당연한 것 같다.
 * 
 */
class BoxingTest {
	@Test
	void testIntPredicate() {
		// 박싱 없음.
		IntPredicate evenNumbers = (int i) -> i % 2 == 0; // 마치 구현체가 만들어져서 참조변수에 들어간 상태나 마찬가지 같다.
		assertTrue(evenNumbers.test(1000));

		// 박싱 있음.
		Predicate<Integer> oddNumbers = (Integer i) -> i % 2 == 1;
		assertFalse(oddNumbers.test(1000));
	}

}
