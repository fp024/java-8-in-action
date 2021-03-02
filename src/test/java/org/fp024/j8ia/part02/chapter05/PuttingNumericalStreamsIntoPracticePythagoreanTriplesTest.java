package org.fp024.j8ia.part02.chapter05;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 5.6.3 숫자 스트림 활용: 피타고라스 수
 * 
 * 공식:  a * a + b * b = c * c
 *        a * a + b * b 의 루트는 c 가 될 수 있는 것 같다.
 * 
 * 어려운 부분...
 */
@Slf4j
class PuttingNumericalStreamsIntoPracticePythagoreanTriplesTest {

	/**
	 * 좋은 필터링 조합
	 * 
	 * 아래와 같이했을 때는 중복이 되는 부분이있음.
	 */
	@Test
	void testFilteringGoodCombinations() {
		IntStream aStream = IntStream.rangeClosed(1, 20);

		aStream.forEach(a -> {
			IntStream.rangeClosed(1, 10).filter(
					b -> Math.sqrt(a * a + b * b) % 1 == 0
			).forEach(
					b -> logger.info("a:{}, b:{}", a, b)
			);
		});
	}
	
	/**
	 * 중복을 없애기 위해서 두번째 수는
	 * b의 스트림 시작값을 현재 a부터 시작하게해야한다. 
	 */
	@Test
	void testFilteringGoodCombinationsDistinct() {	
		IntStream aStream = IntStream.rangeClosed(1, 20);

		aStream.forEach(a -> {
			IntStream.rangeClosed(a, 20).filter(
					b -> Math.sqrt(a * a + b * b) % 1 == 0
			).forEach(
					b -> logger.info("a:{}, b:{}", a, b)
			);
		});
	}
	
	
	/**
	 * 집합 생성 및 b값 생성
	 */
	@Test
	void testGeneratingTuplesAndBValues() {
		/*
		int a = 3;  // a에 대해 미리 반복을 생각하지 말고 요소하나로 우선 생각하고 나중에 해결하는것이 덜해깔린다.
		IntStream.rangeClosed(1,  5)
			.filter(b-> Math.sqrt( a*a + b*b) % 1 ==0)
			.map(b-> new int[] {a, b, Math.sqrt(a*a+ b*b)});
			 // map은 int를 반환하길 기대하는데, int[]를 반환하려하려하여 오류
			 // Math.sqrt()는 double로 반환하기 때문에, (int) 캐스팅이 필요하다.
		*/

		int a = 3;  // a에 대해 미리 반복을 생각하지 말고 요소하나로 우선 생각하고 나중에 해결하는것이 덜해깔린다.
		IntStream.rangeClosed(1,  5)
				.filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
				.mapToObj(b -> new int[] { a, b, (int) Math.sqrt(a * a + b * b) }); // Stream<int[]>

	}
	
	
	/**
	 * a 값 생성 및 코드 실행
	 */
	@Test
	void testGeneratingAValuesAndRunningCode() {
		Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1, 100)
			.boxed() // Stream<Integer>, Boxing을 안하면 반환을 IntStream으로 하려한다.
			.flatMap( a ->  // map()을 쓸 경우 Stream<Object>를 반환
					IntStream.rangeClosed(a,  100)
					.filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
					.mapToObj(b -> new int[] { a, b, (int) Math.sqrt(a * a + b * b)})
		);
			
		pythagoreanTriples
			.limit(5)
			.forEach(t -> logger.info("{}, {}, {}", t[0], t[1], t[2]));
	}
	
	
	/**
	 * 개선 포인트
	 * 
	 *    sqrt를 두번 하는 점을 개선
	 *    
	 *    저자분은 map으로 결과 배열을 일단 만들고 
	 *    결과 배열에서 정수로 떨어지지 않는 결과값의 필터링을 나중에 하는식으로
	 *    해결했다.
	 *    
	 */
	@Test
	void testDoBetter() {
		Stream<double[]> pythagoreanTriples = IntStream.rangeClosed(1, 100)
			.boxed() // Stream<Integer> Boxing을 안하면 반환을 IntStream으로 하려한다.
			.flatMap( a ->  // map()을 쓸 경우 Stream<Object>를 반환
					IntStream.rangeClosed(a,  100)
					.mapToObj(b -> new double[] { a, b, Math.sqrt(a * a + b * b)})
					.filter(t -> t[2] % 1 == 0)		
			);

		pythagoreanTriples
			.limit(5)
			.forEach(t -> logger.info("{}, {}, {}", t[0], t[1], t[2]));
	}
}
