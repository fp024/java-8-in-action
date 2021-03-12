package org.fp024.j8ia.part02.chapter05;

import org.fp024.j8ia.part02.chapter06.CaloricLevel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 음식 p131. 저자님이 음식 도메인에 대한 정의를 해줬다.
 */
@Getter
@AllArgsConstructor
public class Dish {
	private final String name;
	private final boolean vegetarian;
	private final int calories;
	private final Type type;

	public enum Type {
		MEAT, FISH, OTHER
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * 8장 8.1.3절에서 사용한다.
	 */
	public CaloricLevel getCaloricLevel() {
		if (getCalories() <= 400) {
			return CaloricLevel.DIET;
		} else if (getCalories() <= 700) {
			return CaloricLevel.NORMAL;
		} else {
			return CaloricLevel.FAT;
		}
	}
}
