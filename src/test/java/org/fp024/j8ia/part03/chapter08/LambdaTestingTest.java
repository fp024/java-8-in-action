package org.fp024.j8ia.part03.chapter08;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

/**
 * 8.3 람다 테스팅
 * 
 * 저자님 스타일이 .. 객체 상태를 바꾸지 않는 스타일로 구현하는 분 같음..ㅎㅎ 
 */
class LambdaTestingTest {
	@Test
	void testMoveRightBy() {
		Point p1 = new Point(5,5);
		Point p2 = p1.moveRightBy(10);
		assertEquals(15 , p2.getX());
		assertEquals(5 , p2.getY());
	}
	
	/**
	 * 8.3.1 보이는 람다 표현식의 동작 테스팅
	 */
	@Test
	void testComparingTwoPoints() {
		Point p1 = new Point(11, 15);
		Point p2 = new Point(10, 20);
		// 우선순위가 있는 비교
		//    x기준으로 우선 비교하고, x가 같다면 y로 비교.
		int result = Point.compareByXAndThenY.compare(p1, p2);

		assertEquals(1, result);

		Point p3 = new Point(10, 20);
		Point p4 = new Point(10, 20);
		result = Point.compareByXAndThenY.compare(p3, p4);
		assertEquals(0, result);
	}
	
	
	/**
	 * 8.3.2 람다를 사용하는 메서드의 동작에 집중하라
	 */
	@Test
	void testMoveAllPointsRightBy() {
		List<Point> points = Arrays.asList(new Point(5,5), new Point(10,5));
		//List<Point> expectedPoints = Arrays.asList(new Point(15,5), new Point(20,5));
		LinkedHashSet<Point> expectedPoints = new LinkedHashSet<>();
		expectedPoints.add(new Point(15,5));
		expectedPoints.add(new Point(20,5));
		
		List<Point> newPoints = Point.moveAllPointsRightBy(points, 10);
		
		
		// 실패함. List와 LinkedHashSet의 비교 실패
		// assertEquals(expectedPoints, newPoints);
		
		// 반복가능 항목이 반드시 동일할 필요없으나 순서는 맞아야함 
		assertIterableEquals(expectedPoints, newPoints);
	}
	
	

	/**
	 * 8.3.3 복잡한 람다를 개별 메서드로 분할하기
	 * 
	 * 복잡한 람다식의 경우 메서드레퍼런스로 만들어 해당 메서드를 테스트 하도록 한다.
	 * ==> 예전에 칼로리 계산 수식을 도메인 정적 메서드로 만들어 포함시키고,
	 *      그것을 외부에서 메서드 레퍼런스로 호출했던것 처럼... 
	 *  
	 */
	

	/**
	 * 8.3.4 고차원 함수 테스팅
	 * 
	 *  고차원함수: 함수를 인수로 받거나 다른 함수를 반환하는 메서드
	 */
	@Test
	void testFilter() {
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
		List<Integer> even = filter(numbers, i -> i % 2 == 0);
		List<Integer> smallerThanThree = filter(numbers, i -> i < 3);

		assertEquals(Arrays.asList(2, 4), even);
		assertEquals(Arrays.asList(1, 2), smallerThanThree);
	}

	static List<Integer> filter(List<Integer> numbers, Predicate<Integer> p) {
		List<Integer> result = new ArrayList<>();
		for (Integer number : numbers) {
			if (p.test(number)) {
				result.add(number);
			}
		}
		return result;
	}
}
