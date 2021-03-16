package org.fp024.j8ia.part03.chapter09;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import org.fp024.j8ia.part03.chapter09.QuizRememberTheResolutionRules.C;
import org.junit.jupiter.api.Test;

/**
 * 9장 디폴트 메서드
 *
 *   1. 인터페이스 내부에 정적 메서드 제공
 *   2. 디폴트 메서드: 인터페이스의 기본 구현 제공 
 */
class DefaultMethodsTest {

	/**
	 * 9.1 변화하는 API
	 * 	 1. 바이러니 호환성
	 * 		뭔가를 바꾼 이후에도 에러없이 기존 바이너리가 실행될 수 있는 상황
	 *  	상위 인터페이스에 메서드가 추가되었지만 사용처에서, 해당 메서드를 사용하지 않아 문제가 없는상황
	 *  	(사용처는 재컴파일 하지 않은 상태)
	 * 
	 * 	2. 소스 호환성 
	 * 		코드를 고쳐도 성공적으로 코드를 재컴파일 할 수 있음을 의미함.
	 * 		(인터페이스가 바뀌었다면 소스 재컴파일시 오류가남)
	 * 
	 *  3. 동작 호환성
	 *  	코드를 바꾼 뒤에도 같은 값이 주어지면 프로그램이 같은 동작을 하는 의미.
	 * 
	 */

	
	
	/**
	 * 9.2 디폴트 메서드란 무엇인가?
	 * 
	 *  0. 인터페이스에 정의
	 * 	1. defalut 키워드로 시작
	 *  2. 메서드에 바디를 포함
	 *   
	 *   추상 클래스와 자바8의 인터페이스 차이
	 *   1. 클래스는 하나의 추상 클래스만 상속 받을 수 있지만 인터페이스는 여러개를 구현 가능.
	 *   2. 추상 클래스는 인스턴스 변수로 공통 상태를 가질 수 있지만, 인터페이스는 인스턴스 변수를 가질 수 없음.
	 * 
	 */
	
	
	/**
	 * 퀴즈 9-1
	 * 	ArrayList, TreeSet, LinkedList 및 다른 모든 컬랙션에서 사용할 수 있는 removeIf를 추가해달라는 요청이
	 * 있을 때, 새로운 removeIf를 기존 컬렉션 API에 가장 적절하게 추가할 수 있는 방법
	 */
	@Test
	void testQuizAddRemoveIf() {
		Collection<String> a = new ArrayList<String>();
		a.add("a");
		a.add("b");
		a.add("c");
		Collection<String> b = new TreeSet<String>();
		Collection<String> c = new LinkedList<String>();
		
		// 위의 3개 메서드가 전부 Collection 인터페이스를 구현한다.

		// Collection에 removeIf 메서드를 디폴트 메서드로 추가하는게 답이라고 생각하는데...
		// Collection에 이미 default로 구현이 되어있습니다.
		
		/*// AdoptOpenJDK 1.8.0_282 구현
		default boolean removeIf(Predicate<? super E> filter) {
	        Objects.requireNonNull(filter);
	        boolean removed = false;
	        final Iterator<E> each = iterator();
	        while (each.hasNext()) {
	            if (filter.test(each.next())) {
	                each.remove();
	                removed = true;
	            }
	        }
	        return removed;
    	}
		*/
		assertTrue(a.removeIf(s -> s.equals("b")));
		assertEquals(Arrays.asList("a","c"), a);

		assertFalse(b.removeIf(s -> s.equals("b")));
		assertFalse(c.removeIf(s -> s.equals("b")));
	}
	
	
	/**
	 * 9.3 디폴트 메서드 활용패턴
	 * 		1. 선택형 메서드
	 * 		2. 동작 다중 상속
	 */
	
	
	/**
	 * 9.3.1 선택형 메서드
	 * 		불필요하게 구현 클래스에서 빈구현을 제공했던 상황이 있었는데..
	 * 		인터페이스에서 default 메서드를 사용해 기본 구현을 제공해서 그럴필요가 없다.
	 */
	@Test
	void testOptinalMethods() {
		Iterator<String> a = new Iterator<String>() {
			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public String next() {
				return null;
			}
		};
		
		// 기본 구현이 지원하지 않는 동작이라고 예외를 발생시킴..
		assertThrows(UnsupportedOperationException.class, () -> a.remove());
	}
	
	
	/**
	 * 9.3.2 동작 다중 상속
	 */
	@Test
	void testMultipleInheritanceOfBehavior() {
		// 생성자는 내부적으로 죄표 높이 너비 기본 각도를 설정한다고 하는데,
		// 예제상 특별히 설정 부분이 없어서 Monster에 설정한 기본 맴버변수의
		// 기본 타입으로 설정되어서 width, height, x, y, rotationAngle 값들이 기본으로는 0이다.
		
		Monster m = new Monster();
		m.rotateBy(180);
		m.moveVertically(10);
		
		assertEquals(180, m.getRotationAngle());
		assertEquals(10, m.getY());
	}
	
	
	/**
	 * 9.4 해석 규칙
	 */
	@Test
	void testResolutionRules() {
		// 오버라이드를 했기 때문에 D가 호출 되길 기대했는데 맞다.
		assertEquals("Hello from D", C.getHello());
	}
	
	/*
	 * 9.4.1 3가지 해결 규칙
	 * 
	 * 	1. 클래스가 항상 이김
	 * 		클래스나 슈퍼 클래스에서 정의한 메서드가 디폴트 메서드 보다 우선권을 가짐.
	 *  2. 1번 규칙 이외의 상황에서는 서브인터페이스가 이김.
	 *  
	 *  3. 여전히 디폴트 메서드의 우선순위가 결정되지 않는다면...
	 *     구현 클래스에서 디폴트 메서드를 오버라이드해서 호출해야함.
	 * 
	 */
	
	
	/**
	 * 9.4.2 디폴트 메서드를 제공하는 서브 인터페이스가 이김.
	 *       가장 구체적인 기본 제공 인터페이스가 이김.
	 */
	@Test
	void testMostSpecificDefaultProvidingInterfaceWins() {
		// C가 D를 상속받아서 D에서 암묵적으로 사용할 수 있는 A의 hello가 실행될 줄알았는데.. 
		//   (default 메서드의 오버라이드가 아닌 단순 구현이라 졌음.) 
		// A의 서브인터페이스인 B가 이겼음.
		assertEquals("Hello from B", MostSpecificDefaultProvidingInterfaceWins.C.getHello());
	}
	
	
	@Test
	void testQuizRememberTheResolutionRules() {
		assertEquals("Hello from D", QuizRememberTheResolutionRules.C.getHello());
	}
	
	@Test
	void testConflictsAndExplictDisambiguation() {
		assertEquals("Hello from B", new ConflictsAndExplictDisambiguation.C().hello());
	}
	
	@Test
	void testDiamondProblem() {
		assertEquals("Hello from D" , DiamondProblem.D.getMessage());
	}
	
	
	/*
	 * 9.5 요약
	 * 
	 * - 클래스나 슈퍼 클래스에 정의된 메서드가 다른 디폴트 메서드 정의보다 우선함.
	 *   그 이외 상황에서는 서브 인터페이스에서 제공하는 디폴트 메서드가 선택됨.
	 * 
	 */
}
