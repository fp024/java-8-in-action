package org.fp024.j8ia.part01.chapter03;

import java.util.Arrays;
import java.util.List;

/**
 * 2장 테스트용 데이터 레파지토리
 */
public class AppleRepository {
	public static List<Apple> getAppleList() {
		return Arrays.asList(
				Apple.builder().color("green").weight(100).country("Korea").build(),
				Apple.builder().color("red").weight(160).country("USA").build(), 
				Apple.builder().color("green").weight(150).country("France").build(),
				Apple.builder().color("yellow").weight(80).country("Chile").build(),
				Apple.builder().color("red").weight(80).country("Korea").build(),
				Apple.builder().color("white").weight(180).country("Japan").build(),
				Apple.builder().color("red").weight(140).country("Korea").build());
	}
}
