package org.fp024.j8ia.part03.chapter09;

import org.fp024.j8ia.part03.chapter09.ResolutionRules.A;
import org.fp024.j8ia.part03.chapter09.ResolutionRules.B;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class QuizRememberTheResolutionRules {
	static class D implements A {
		@Override
		public String hello() {  // 이부분이 반드시 public 이 되야할 것 같은데...
			return "Hello from D";
		}
	}

	static class C extends D implements B, A {
		static String getHello() {
			return new C().hello();
		}
	}
}
