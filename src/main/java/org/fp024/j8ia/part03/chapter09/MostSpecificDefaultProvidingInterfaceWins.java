package org.fp024.j8ia.part03.chapter09;

import org.fp024.j8ia.part03.chapter09.ResolutionRules.A;
import org.fp024.j8ia.part03.chapter09.ResolutionRules.B;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 같은 패키지 레벨에서 이름 중복이 생기므로 이제부터는 특정 클래스 안의 내부 클래스로 넣는다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MostSpecificDefaultProvidingInterfaceWins {
	static class D implements A { }
	
	static class C extends D implements B, A {
		static String getHello() {
			return new C().hello();
		}
	}
}
