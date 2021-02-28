package org.fp024.j8ia.part02.chapter04;

import java.util.Arrays;
import java.util.List;

/**
 * 4장 - 스트림 소개 테스트용
 */
public class MyDishRepository {
	private MyDishRepository() {
		// 유틸리티 클래스 객체 생성 방지
	}
	public static List<MyDish> getDishList() {
		return Arrays.asList(
				MyDish.builder().name("나가사끼 짬뽕").calories(500).build()
			  , MyDish.builder().name("켈로그 콘푸로스트").calories(300).build()
			  , MyDish.builder().name("고메 함박스테이크").calories(250).build()
			  , MyDish.builder().name("성경 파래김").calories(25).build()
			  , MyDish.builder().name("계란").calories(155).build()
			  , MyDish.builder().name("신선 설렁탕").calories(490).build()
			  , MyDish.builder().name("육대감 육개장").calories(510).build()
		);
	}
}
