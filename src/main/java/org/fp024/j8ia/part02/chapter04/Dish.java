package org.fp024.j8ia.part02.chapter04;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 음식 p131. 저자님이 음식 도메인에 대한 정의를 해줬다.
 */
@Getter
@AllArgsConstructor
@ToString(of = { "name" })
public class Dish {
	private final String name;
	private final boolean vegetarian;
	private final int calories;
	private final Type type;

	public enum Type {
		MEAT, FISH, OTHER
	}
}
