package org.fp024.j8ia.part03.chapter09;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 퀴즈 9-3 거의 비슷한 시그니처
 * 	
 * => 반환 타입만 다른 메서드가 2개가 생성되는 식이 되는것이여서,
 *    반드시 컴파일 오류가 발생. 명시적인 해결방법을 사용해도 해결할 수 없다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizAlmostTheSameSignature {
	interface B {
		public default Integer getNumber() {
			return 42;
		}
	}
	
	interface A {
		public default String getNumber() {
			return "10";
		}
	}
	
	/**
	 * 리턴 타입이 다르긴 하나 메서드 명과 파라미터가 같아 중복으로 판단하는 것 같다.
	 * 컴파일 오류가 나고, 어느 하나를 선택해야한다.
	 * 
	 * Duplicate default methods named getNumber with the parameters () and () are inherited
	 *  from the types QuizAlmostTheSameSignature.A and QuizAlmostTheSameSignature.B
	 */
	/*
	static class C implements B, A {
		static Number getData() {
			return new C().getNumber();
		}
	} 
	*/

	/*
	static class C implements B, A {		
		// 반환 타입이 다른 디폴트 메서드의 오버라이드는 
		// 항상 오류가 난다.
		// 타입을 맞춰주더라도 다른 한쪽과 타입이 안맞고 나온다는 둥 해결할 수 없는 문제가 발생한다. ㅠㅠ 
		// The return type is incompatible with QuizAlmostTheSameSignature.A.getNumber()
		
		// The return type is incompatible with QuizAlmostTheSameSignature.B.getNumber()
		
		@Override
		public String getNumber() {
			return A.super.getNumber();
		}

		@Override
		public Integer getNumber() {
			return B.super.getNumber();
		}

		static Number getData() {
			return new C().getNumber();
		}
	}
	*/ 

	
	/**
	 * 반환 결과가 다른 파라미터, 메서드 명이 같은 함수 지정하는 결과나 마찬가지가 되어
	 * 문제가 생기는 것 같다.
	 * 
	 * Duplicate method getNumber() in type QuizAlmostTheSameSignature.Test
	 * 
	 * 아래와 같은 형태의 클래스를 만들 수가 없음.
	 */
	/*
	static class DuplicateMethodClass {
		String getNumber() {
			return "10";
		}
		
		Integer getNumber() {
			return 1;
		}
	}
	*/
}
