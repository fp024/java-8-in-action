package org.fp024.j8ia.part02.chapter06;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.minBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.summarizingInt;
import static java.util.stream.Collectors.summingInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.fp024.j8ia.part02.chapter05.Dish;
import org.fp024.j8ia.part02.chapter05.DishRepository;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 6.2 리듀싱과 요약
 */
@Slf4j
class ReducingAndSummarizingTest {
	// 5장에서 사용한 데이터를 재사용한다.
	private final List<Dish> menu = DishRepository.getDishList();

	// counting 메서드로 요리 수 계산.
	@Test
	void testCounting() {
		assertEquals(menu.size(), menu.stream().collect(counting()));
		assertEquals(menu.size(), menu.stream().count());
	}
	
	/**
	 * 6.2.1 스트림 값에서 최댓값과 최소값 검색
	 */
	@Test
	void testFindingMaximunAndMinimumInAStreamOfValuesTest() {
		Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
		Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));

		assertTrue(mostCalorieDish.isPresent());
		assertEquals("pork", mostCalorieDish.get().getName());

		Optional<Dish> lessCalorieDish = menu.stream().collect(minBy(dishCaloriesComparator));
		assertTrue(mostCalorieDish.isPresent());
		assertEquals("season fruit", lessCalorieDish.get().getName());		
	}
	
	
	/**
	 * 6.2.2 요약 연산
	 */
	@Test
	void testSummarization() {
		int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
		assertEquals(4200, totalCalories); // 책의 도표가 4300으로 나와있는데, 4장에서 사용한 요리 데이터 기준으로는 4200이 맞음.
		
		double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));
		assertTrue(avgCalories > 466);

		IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
		logger.info(menuStatistics.toString());		
	}
	
	
	/**
	 * 6.2.3 문자열 연결
	 */
	@Test
	void testJoiningStrings() {
		String shortMenu = menu.stream().map(Dish::getName).collect(joining());	
		logger.info(shortMenu);
		
		// Eclipse AdoptJDK 1.8.0_282 에서 테스트시 컴파일 오류가 난다. String으로 넘겨줘야 정상동작함.
		// The method collect(Collector<? super Dish,A,R>) in the type Stream<Dish> is not applicable 
		//    for the arguments (Collector<CharSequence,capture#15-of ?,String>)
		// String shortMenuToString = menu.stream().collect(joining());
		String shortMenuToString = menu.stream().map(Dish::toString).collect(joining());
		logger.info(shortMenuToString);
		
		assertEquals(shortMenuToString, shortMenu);

		String shortMenuUseComma = menu.stream().map(Dish::getName).collect(joining(", "));	
		logger.info(shortMenuUseComma);		
	}
	
	
	
	/**
	 * 6.2.4 범용 리듀싱 요약 연산
	 */
	@Test
	void testGeneralizedSymmarizationWithReduction() {
		assertEquals(4200, menu.stream().collect(reducing(0 /** 리듀싱 연산의 시작값 : the identity value for the reduction (also, the value that is returned when there are no input elements)  */
				, Dish::getCalories /** 요리를 칼로리정수로 변환해주는 함수 :  a mapping function to apply to each input value */
				, (i, j) -> i + j)) /** a BinaryOperator<U> used to reduce the mapped values */
		);
		
		Optional<Dish> mostCalorieDish = menu.stream().collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
		assertTrue(mostCalorieDish.isPresent());
		assertEquals(800, mostCalorieDish.get().getCalories());
	}
	
	
	/**
	 * collect 와 reduce
	 * 
	 * collect:
	 * 	도출하려는 결과를 누적하는 컨테이너를 바꾸도록 설계된 메서드
	 * 
	 * reduce:
	 * 	두 값을 하나로 도출하는 불변형 연산
	 *   reduce는 누적자로 사용된 리스트 변환하므로 잘못된 사용.
	 * 
	 *   가변 컨테이너 관련 작업이면서 병렬성을 확보하려면 collect 메서드로
	 *   리듀싱 연산을 구현하는 것이 올바름.
	 */
	@Test
	void collectVsReduceTest() {
		List<Integer> expect = Arrays.asList(1, 2, 3, 4, 5, 6);
		Stream<Integer> stream = expect.stream();
		
		List<Integer> actual = stream.reduce(
				new ArrayList<Integer>(), /** 초기값 누적자 */
				(List<Integer> l, Integer e) -> {
					l.add(e);
					return l;
				}
				,
				(List<Integer> l1, List<Integer> l2) -> { // 다음 연산된 l1이 l2로 들어오는 것 같다.
					l1.addAll(l2);
					return l1;
				}
		);
		
		assertIterableEquals(expect, actual);
	}
	

	/**
	 * 컬렉션 프레임 워크 유연성: 같은 연산도 다양한 방식으로 수행할 수 있음.
	 */
	@Test
	void testCollectionFrameworkFlexibility() {
		// Integer 클래스의 sum 메서드 레퍼런스
		int totalCalories = menu.stream().collect(reducing(
				  0 /* 초기값 */
				, Dish::getCalories /* 변환함수 */
				, Integer::sum /* 합계 함수 */
				)
		);
		assertEquals(4200, totalCalories);

		// reducing 사용
		Integer totalCaloriesUseReduce = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
		assertEquals(totalCalories, totalCaloriesUseReduce);
		
		// IntStream의 sum 사용 (저자님이 가장 좋고(성능도(boxing 문제 없음)) 간결하다고 함)
		int totalCaloriesUseIntStreamSum = menu.stream().mapToInt(Dish::getCalories).sum();
		assertEquals(totalCalories, totalCaloriesUseIntStreamSum);
	}
	
	
	/**
	 * 퀴즈 6-1 리듀싱으로 문자열 연결하기
	 * 
	 * 1, 3 번이 사용가능을 보여줬기는 했으나  joining을 사용하는 것이 바람직하다고 함.
	 */
	@Test
	void testQuizJoiningStringsWithReducing() {
		String shortMenu = menu.stream().map(Dish::getName).collect(joining());
		
		// 1. Collectors의 reducing 메서드 사용: 올바름.
		assertEquals(shortMenu
				, menu.stream().map(Dish::getName).collect(reducing((s1, s2) -> s1 + s2)).get());
		
		// 2. 이거 왠지 reducing의 반환값이 Dish가 되야하는데.. String으로 반환하려해서 컴파일 오류일 것 같은데...
		//    컴파일 오류가 아래와 같이 발생한다.
		//        Type mismatch: cannot convert from String to Dish
		//    BinaryOperator 가 상위 BiFunction의 apply 를 사용하게되는데.. 이때 두개 전달 파라미터 타입과 반환 타입이 T타입으로 설정되서,
		//    Dish를 반환해야함.
		//       BinaryOperator<T> extends BiFunction<T,T,T>
		//         
		// menu.stream().collect(reducing((d1, d2)-> d1.getName() + d2.getName()));
		
		
		// 3. 3개 인자를 받는 reducing 사용 (초기값, 변환함수, 합계합수) : 올바름.
		assertEquals(shortMenu,  
				menu.stream().collect(reducing("", Dish::getName, (s1, s2) -> s1 + s2)));
	}
}

