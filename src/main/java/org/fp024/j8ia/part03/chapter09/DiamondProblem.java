package org.fp024.j8ia.part03.chapter09;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class DiamondProblem {
	interface A {
		default String hello() {
			return "Hello from A";
		}
	}

	interface B extends A {
	}

	interface C extends A {
		@Override
		String hello();
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	static class D implements B, C {
		/**
		 * 최상위 A에 default 메서드가 있더라도 하위 인터페이스에서 interface 메서드를 재정의 했다면
		 * 하위 상속 클래스에서는 구현 해야한다.
		 */
		@Override
		public String hello() {
			return "Hello from D";
		}
		
		static String getMessage() {
			return new D().hello();
		}
	}

}
