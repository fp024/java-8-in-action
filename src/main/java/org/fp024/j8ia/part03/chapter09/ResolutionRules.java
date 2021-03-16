package org.fp024.j8ia.part03.chapter09;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

class ResolutionRules {
	interface A {
		default String hello() {
			return "Hello from A";
		}
	}

	interface B extends A {
		@Override
		default String hello() {
			return "Hello from B";
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	static class C implements B, A {
		static String getHello() {
			return new C().hello();
		}
	}
}