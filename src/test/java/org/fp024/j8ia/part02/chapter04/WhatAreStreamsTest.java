package org.fp024.j8ia.part02.chapter04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import lombok.extern.slf4j.Slf4j;

/**
 * 4.1 스트림이란 무엇인가?
 */
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
class WhatAreStreamsTest {
	/** 
	 * 동작을 보니 멤버변수에 직접 초기화하면 테스트 메서드마다 초기화가 이루어지는 것 같다. 
	 * 단순한 내용이라면 @BeforeEach 를 붙인 메서드내에서 초기화 해줄 필요가 없음.
	 */
	private List<MyDish> menu = MyDishRepository.getDishList();
	
	@Order(1)
	@Test
	void testLegacy() {
		// 누적자로 요소 필터링
		List<MyDish> lowCaloricDishes = new ArrayList<>();
		for (MyDish d : menu) {
			if (d.getCalories() < 400) {
				lowCaloricDishes.add(d);
			}
		}

		// 익명 클래스로 요리정렬
		Collections.sort(lowCaloricDishes, new Comparator<MyDish>() {
			@Override
			public int compare(MyDish d1, MyDish d2) {
				return Integer.compare(d1.getCalories(), d2.getCalories());
			}
		});

		// 정렬된 리스트를 처리하면서 요리 이름 선택
		List<String> lowCaloricDishesName = new ArrayList<>();
		for (MyDish d : lowCaloricDishes) {
			lowCaloricDishesName.add(d.getName());
		}

		logger.info("====================================");
		lowCaloricDishes.forEach(d -> logger.info(d.toString()));
		logger.info("====================================");
		
		assertEquals(25, lowCaloricDishes.get(0).getCalories());
		assertEquals("성경 파래김", lowCaloricDishesName.get(0));

	}

	/**
	 * 메서드 레퍼런스를 바로 적용하면 좀 해깔려서, 람다식으로 써본다.
	 */
	@Order(2)
	@Test
	void testJava8UseLambda() {
		List<String> lowCaloricDishesName = menu.stream()
			.filter(d -> d.getCalories() < 400)
			.sorted((d1, d2) -> Integer.compare(d1.getCalories(), d2.getCalories()))
			.map(d -> d.getName())
			.collect(Collectors.toList());
		
		logger.info("====================================");
		lowCaloricDishesName.forEach(d -> logger.info(d.toString()));
		logger.info("====================================");
		
		assertEquals("성경 파래김", lowCaloricDishesName.get(0));
	}
	
	
	@Order(2)
	@Test
	void testJava8UseMetohdRef() {
		logger.info("====================================");
		List<String> lowCaloricDishesName = menu.stream()
			.filter(d -> d.getCalories() < 400)
			.sorted(Comparator.comparing(MyDish::getCalories))
			.map(MyDish::getName)
			.collect(Collectors.toList());
		lowCaloricDishesName.forEach(d -> logger.info(d.toString()));
		logger.info("====================================");
		
		assertEquals("성경 파래김", lowCaloricDishesName.get(0));
	}

	
	/**
	 *  menu.parallelStream() 을 대신 쓰면 멀티코어 아키텍쳐에서 병렬 실행가능, 
	 *  그런데 요소가 얼마 없어서 싱글에 비해 시간이 미세하게 더걸림...ㅎㅎ
	 */
	@Order(2)
	@Test
	void testJava8UseMetohdRefAndParallelStream() {
		logger.info("====================================");
		List<String> lowCaloricDishesName = menu.parallelStream()  
			.filter(d -> d.getCalories() < 400)
			.sorted(Comparator.comparing(MyDish::getCalories))
			.map(MyDish::getName)
			.collect(Collectors.toList());
		lowCaloricDishesName.forEach(d -> logger.info(d.toString()));
		logger.info("====================================");
		
		assertEquals("성경 파래김", lowCaloricDishesName.get(0));
	}
	
}
