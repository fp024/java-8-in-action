package org.fp024.j8ia.part01.chapter03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.DoubleFunction;

import org.junit.jupiter.api.Test;

/**
 * p123. 3.9.1 적분 ~ 3.9.2 Java 8 Lambda로 연결
 * 
 * 그래프의 영역을 구하는 수식을 나타내는데... 미분, 적분책을 읽으니 수식의 의미가 보인다.
 * 
 * 122쪽에 나타난 그래프 영역이 어떤 직사각형의 절반이라고 생각하고 x영역 값과 y영역값을 곱해서 직사각형을 구하고 그걸 나눠서 그래프
 * 영역을 구하는건데.... 이것을 Java 8 의 람다식을 활용해서 나타내려한다.
 * 
 * 그래프 영역 구하기 (f(a)+f(b) * (b-a)) / 2.0
 */
class MathematicalConceptTest {
	@Test
	void intergrateTest() {
		double x = 7 - 3;
		double y = 17 + 13;
		double funcArea = (x * y) / 2.0;

		assertEquals(funcArea, intergrate(C::f, 3, 7));

	}

	static class C {
		static double f(double x) {
			return x + 10;
		}
	}

	double intergrate(DoubleFunction<Double> f, double a, double b) {
		return (f.apply(a) + f.apply(b)) * (b - a) / 2.0;
	}

}
