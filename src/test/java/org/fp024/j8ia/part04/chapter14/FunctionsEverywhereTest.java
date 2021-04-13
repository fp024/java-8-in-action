package org.fp024.j8ia.part04.chapter14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

import org.fp024.j8ia.part02.chapter05.Transaction;
import org.junit.jupiter.api.Test;

/**
 * 14.1 함수는 모든 곳에 존재한다
 * 
 */
class FunctionsEverywhereTest {
	@Test
	void testMethodReference() {
		Function<String, Integer> strToInt = Integer::parseInt;
		assertEquals(12345, strToInt.apply("12345"));
	}

	@Test
	void testHigherOrderFunctionsTest() {
		Function<Double, Double> a = (Double x) -> x * x;
		Function<Function<Double, Double>, Function<Double, Double>> c = (b) -> b.andThen(x -> 2 * x);
		assertEquals(18, c.apply(a).apply(3.0));
	}

	// 섭씨를 화시로 계산하는 함수
	// Function<Double, Double> cToF = (Double x) -> x * 9 / 5 + 32;

	/**
	 * 변환 패턴으로 나타냄.
	 * 
	 * @param x 변환하려는 값
	 * @param f 변환 요소
	 * @param b 기준치 조정 요소
	 * @return 변환된 값
	 */
	static double converter(double x, double f, double b) {
		return x * f + b;
	}

	/**
	 * 커링이라는 개념을 활용해서 한개의 인수를 갖는 변환함수 생성 -> x와 y라는 인자를 받는 함수 f를 한 개의 인수를 받는 g라는 함수로
	 * 대체하는 기법
	 */
	static DoubleUnaryOperator curriedConverter(double f, double b) {
		return (double x) -> x * f + b;
	}

	@Test
	void testCurrying() {
		DoubleUnaryOperator convertCtoF = curriedConverter(9.0 / 5, 32);
		assertEquals(89.6, convertCtoF.applyAsDouble(32));

		// 달러 대비 파운드 환율은 저자님 시점과 다르긴하니..
		DoubleUnaryOperator convertUSDtoGBP = curriedConverter(0.6, 0);
		assertEquals(0.6, convertUSDtoGBP.applyAsDouble(1));

		// 킬로미터를 마일로...
		DoubleUnaryOperator convertKmtoMi = curriedConverter(0.6214, 0);
		assertEquals(0.6214 * 2, convertKmtoMi.applyAsDouble(2));
	}

	static TrainJourney link(TrainJourney a, TrainJourney b) {
		if (a == null) {
			return b;
		}
		TrainJourney t = a;
		while (t.getOnward() != null) {
			t = t.getOnward();
		}
		t.setOnward(b);
		return a;
	}

	@Test
	void testSideEffectLink() {
		TrainJourney seoul = new TrainJourney(1000, "서울역", null);

		TrainJourney kumi = new TrainJourney(2000, "구미역", null);

		TrainJourney busan = new TrainJourney(3000, "부산역", null);

		// 서울역에서 구미역 까지의 기차 여행
		TrainJourney sToK = link(seoul, kumi);
		assertEquals("구미역", sToK.getLastOnward().getStation());

		// 서울역에서 부산까지의 기차 여행
		TrainJourney sToB = link(seoul, busan);
		assertEquals("부산역", sToB.getLastOnward().getStation());

		// 저자님이 말하고자 하는 부분이...
		// 위와 같은 식으로 link를 기존정보를 바꾸게 되는 상태가 될 때..
		// 서울에서 구미까지의 연결이 뒤의 부산역 연결 link 작업에 의해 바뀌는 상태가 되는 문제를 지적했다.
		assertEquals("부산역", sToK.getLastOnward().getStation());
	}

	/**
	 * 부작용이 없는 Link
	 */
	static TrainJourney append(TrainJourney a, TrainJourney b) {
		if (a == null) {
			return b;
		} else {
			return new TrainJourney(a.getPrice(), a.getStation(), append(a.getOnward(), b));
		}
	}

	/**
	 * 부작용이 없는 Append
	 */
	@Test
	void testNoSideEffectAppend() {
		TrainJourney seoul = new TrainJourney(1000, "서울역", null);

		TrainJourney kumi = new TrainJourney(2000, "구미역", null);

		TrainJourney busan = new TrainJourney(3000, "부산역", null);

		// 서울역에서 구미역 까지의 기차 여행
		TrainJourney sToK = append(seoul, kumi);
		assertEquals("구미역", sToK.getLastOnward().getStation());

		// 서울역에서 부산까지의 기차 여행
		TrainJourney sToB = append(seoul, busan);
		assertEquals("부산역", sToB.getLastOnward().getStation());

		// 연결시점의 상태객체를 새로 만들어서 반환했기 때문에, 새로운 변경이 이전 변경에 영향을 미치지 않는다.
		assertEquals("구미역", sToK.getLastOnward().getStation());
	}

}
