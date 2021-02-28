package org.fp024.j8ia.part02.chapter04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 4.4 스트림 연산
 */
@Slf4j
class StreamOperationsTest {
	private List<Dish> menu = DishRepository.getDishList();
	
	/**
	 * 4.4.1 중간연산
	 */
	@Test
	void intermediateOperationsTest() {
		Stream<String> stream = menu.stream().filter(d -> {
			logger.info("filtering: {}", d.getName());
			return d.getCalories() > 300;
		}).map(d -> {
			logger.info("mapping: {}", d.getName());
			return d.getName();
		})
		.limit(3);
		// 여기까진 아무것도 로깅 되지 않았다.
		
		List<String> names = stream.collect(Collectors.toList());
		// collect 실행시점에 filter와 map에 추가한 logger가 실행된다.
		
		logger.info(names.toString());
	}
	
	
	/**
	 * 4.4.2 최종 연산
	 */
	@Test
	void terminalOperationsTest() {

 		// menu.stream().forEach(logger::info); 는 사용할 수 없다. 
		// logger.info(Dish) 를 처리할 수 없음.
		
		menu.stream().forEach(System.out::println);
		// System.out.println(Object) 가 있어 Dish를 처리할 수 있고, 
		// toString을 호출하여 출력이 가능하다.
	}
	
	
	/**
	 * Quiz4-1 중간 연산과 최종 연산
	 */
	@Test
	void quizIntermediateVsTerminalOperationsTest() {
		long count = menu.stream()
					.filter(d -> d.getCalories() > 300)
					.distinct()
					.limit(3)
					.count();
		
		// Dish 도메인에 equals를 필드 기준으로 재정의하지 않아 
		// 무조건 차이가 있는 것으로 나타날 것으로 보이고
		// 칼로리가 300을 넘는 것은 7개이지만 limit 3으로 제한한다음에 
		// 카운트 했기 때문에, 결과는 3이겠다.
		
		assertEquals(3, count);
	}
	
	
	
	
	
}
