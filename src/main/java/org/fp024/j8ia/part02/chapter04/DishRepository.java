package org.fp024.j8ia.part02.chapter04;

import java.util.Arrays;
import java.util.List;

/**
 * 4장 - 스트림 소개 테스트 용 - 저자님 도메인에 맞춤.
 */
public class DishRepository {
	private DishRepository() {
		// 유틸리티 클래스 객체 생성 방지
	}

	public static List<Dish> getDishList() {
		return Arrays.asList(
 				  new Dish("pork", false, 800, Dish.Type.MEAT)
				, new Dish("beef", false, 700, Dish.Type.MEAT)
				, new Dish("chicken", false, 400, Dish.Type.MEAT)
				, new Dish("french fries", true, 530, Dish.Type.OTHER)
				, new Dish("rice", true, 350, Dish.Type.OTHER)
				, new Dish("season fruit", true, 120, Dish.Type.OTHER)
				, new Dish("pizza", true, 550, Dish.Type.OTHER)
				, new Dish("prawns", false, 300, Dish.Type.FISH) // 새우
				, new Dish("salmon", false, 450, Dish.Type.FISH)
		);
	}
}
