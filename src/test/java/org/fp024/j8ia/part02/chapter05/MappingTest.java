package org.fp024.j8ia.part02.chapter05;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 5.2 매핑
 * 
 */
@Slf4j
class MappingTest {
	private List<Dish> menu = DishRepository.getDishList();
	
	/**
	 * 5.2.1 스트림의 각 요소에 함수 적용하기
	 */
	@Test
	void testApplyingAFunctionToEachElementOfAStream() {
		// 이름의 목록을 구한다.
		List<String> dishNames = menu.stream()
			.map(Dish::getName)
			.collect(toList());
		
		dishNames.forEach(logger::info);

		
		List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
		List<Integer> wordLengths = words.stream()
				.map(String::length)
				.collect(toList());
		
		// logger가 단일 메서드로는 String을 기대해서, Integer의 경우는 풀어써줘야한다.
		wordLengths.forEach(i -> logger.info("{}", i));
	
		
		// 요리명의 길이 확인 - map을 여러번 사용.
		List<Integer> dishNameLengths = menu.stream()
			.map(Dish::getName)
			.map(String::length)
			.collect(toList());
		logger.info("=== 요리이름 길이 ===");
		dishNameLengths.forEach(i -> logger.info("{}", i));
		
	}
	
	
	/**
	 * 5.2.2 스트림의 평면화
	 */
	@Test
	void testFlateningStreams() {
		// ["Hello", "World"] => ["H", "e", "l", "o", "W", "r", "d"]
		// 중복을 제거하며 단일 문자열 배열을 만들기..
		List<String> words = Arrays.asList("Hello", "World");
		
		List<String[]> splittedWords = words.stream()
			.map(w -> w.split(""))
			.distinct()
			.collect(toList());
		
		//  [[H, e, l, l, o], [W, o, r, l, d]] 로 나오는데.. 이걸 원한게 아니였음..
		logger.info("{}", splittedWords);
		
	}

	
	/**
	 * map과 Arrays.stream 활용
	 */
	@Test
	void testFlateningStreamsUseArraysStream() {
		// ["Hello", "World"] => ["H", "e", "l", "o", "W", "r", "d"]
		// 중복을 제거하며 단일 문자열 배열을 만들기..
		List<String> words = Arrays.asList("Hello", "World");
		
		List<Stream<String>> splittedWords = words.stream()
			.map(w -> w.split(""))
			.map(Arrays::stream)
			.distinct()
			.collect(toList());
		
		//  이번에도 해결안됨.. List<String>을 기대하는데  List<Stream<String>> 임.. 
		// [java.util.stream.ReferencePipeline$Head@11fc564b, java.util.stream.ReferencePipeline$Head@394a2528]
		logger.info("{}", splittedWords);
		
	}
	
	
	/**
	 * flatMap 사용
	 * 
	 * 스트림의 각 값을 다른 스트림으로 만든다음
	 * 모든 스트림을 하나의 스트림으로 연결하는 기능을 수행.
	 */
	@Test
	void testFlateningStreamsUseFlatMap() {
		// ["Hello", "World"] => ["H", "e", "l", "o", "W", "r", "d"]
		// 중복을 제거하며 단일 문자열 배열을 만들기..
		List<String> words = Arrays.asList("Hello", "World");
		
		List<String> splittedWords = words.stream()
			.map(w -> w.split(""))
			.flatMap(Arrays::stream) // flatMap 사용.
			.distinct()
			.collect(toList());
		
		//  해결됨
		// flatMap은 각 배열을 스트림이 아닌 스트림의 콘텐츠로 매핑함.
		logger.info("{}", splittedWords);
	}

	/**
	 * 퀴즈 5-2  
	 * 
	 * 1번. 숫자 리스트가 주어졌을 때, 각 숫자의 제곱근으로 이루어진 리스트를 반환하시오.
	 * [1,2,3,4,5] => [1,4,9,16,25]
	 */
	@Test
	void testQuiz01() {
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> result = numbers.stream()
				.map(i -> Math.pow(i, 2))  // pow의 반환이 Double이다.
				.map(Double::intValue)
				.collect(toList());
		assertIterableEquals(Arrays.asList(1, 4, 9, 16, 25), result);
	}
	
	/**
	 * 2번. 두 개의 숫자 리스트가 있을 때, 
	 *      모든 숫자 쌍의 리스트를 반환하시오.
	 * 
	 * [1,2,3] 과 [3,4] 가 주어지면 을 반환해야함.
	 * 
	 * [(1,3), (1,4), (2,3), (2,4), (3,3), (3,4)]
	 * 
	 * 
	 * 첨에 해깔린게... 최초 주어지는 데이터를 것을 별도의 배열로 생각하지 않고,
	 * [[1,2,3], [3,4]] 이런 모양으로 하나로 주어진다는 말인줄 알았음.. 
	 * 
	 * map 반환형은 map에 람다식 블록의 리턴 타입으로 결정되는 것을 기억하자.
	 * 
	 * 저자님의 해답을 나중에보니.. 저자님은 List<int[]> 로 반환함.
	 */
	@Test
	void testQuiz02() {
		List<Integer> numberList01 = Arrays.asList(1, 2, 3);
		List<Integer> numberList02 = Arrays.asList(3, 4);

		List<List<Integer>> actual = numberList01.stream()
			.flatMap(t -> {
					return numberList02.stream().map(r -> {
						List<Integer> elements = new ArrayList<>();
						elements.add(t);
						elements.add(r);
						return elements;
					}	
				);
			}).collect(toList());
		
			List<List<Integer>> expect = Arrays.asList(
					Arrays.asList(1, 3), Arrays.asList(1, 4)
				  , Arrays.asList(2, 3), Arrays.asList(2, 4)
				  , Arrays.asList(3, 3), Arrays.asList(3, 4));
		assertIterableEquals(expect, actual);
	}

	/**
	 * 3. 이전 예제에서 합이 3으로 나누어 떨어지는 쌍만 봔한하려면 어떻게 해야할까?
	 * 
	 * 		mapping 하기전에 filtering 하면 될 것 같다.
	 */
	@Test
	void testQuiz03() {
		List<Integer> numberList01 = Arrays.asList(1, 2, 3);
		List<Integer> numberList02 = Arrays.asList(3, 4);

		List<List<Integer>> actual = numberList01.stream()
			.flatMap(t -> {
					return numberList02.stream()
							.filter(r -> (t + r) % 3 == 0) // 매핑 전에 필터링하면 되겠다.		
							.map(r -> Arrays.asList(t, r)
				);
			}).collect(toList());
		
		List<List<Integer>> expect = Arrays.asList(
				  Arrays.asList(2, 4)
				, Arrays.asList(3, 3));
		assertIterableEquals(expect, actual);

	}
}


