package org.fp024.j8ia.part03.chapter09;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 9.4.3 충돌 그리고 명시적인 문제 해결
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ConflictsAndExplictDisambiguation {
	interface A {
		default String hello() {
			return "Hello from A";
		}
	}

	interface B  {
		default String hello() {
			return "Hello from B";
		}
	}
	
	/*
	 * 우선순위를 자동 판단 할 수 없어 컴파일 오류가 발생한다. 
	 *  
	 *	Duplicate default methods named hello with the parameters () and () are inherited 
	 *  from the types ConflictsAndExplictDisambiguation.A and ConflictsAndExplictDisambiguation.B
	 */
	/*
	static class C implements B , A {
		
	}
	*/
	
	static class C implements B, A {
		@Override
		public String hello() {
			return B.super.hello();  // 명시적으로 인터페이스 B의 메서드 선택
		}
	}
}
