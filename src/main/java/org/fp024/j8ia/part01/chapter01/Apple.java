package org.fp024.j8ia.part01.chapter01;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Apple {
	private Integer weight;

	private String color;
	
	
	// 녹색 사과 필터 조건
	public static boolean isGreenApple(Apple apple) {
		return "green".equals(apple.getColor());
	}

	// 무거운 사과 필터링 (150g 이상) 조건
	public static boolean isHeavyApple(Apple apple) {
		return apple.getWeight() >= 150;
	}
}
