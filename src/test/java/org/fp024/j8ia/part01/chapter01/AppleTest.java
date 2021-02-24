package org.fp024.j8ia.part01.chapter01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class AppleTest {
	/**
	 * p35. 첫 Java 8 코드
	 */
	@Test
	void appleTest() {
		List<Apple> inventory = new ArrayList<>();
		IntStream.rangeClosed(1, 5).forEach(i -> {
			Integer randomNumber = Math.abs(new Random().nextInt(50)) + 1;
			inventory.add(Apple.builder().weight(randomNumber).build());
		});

		logger.info("======= 정렬 전 =======");
		inventory.forEach(apple -> logger.info("{}", apple));

		// Collections.sort가 배열자체의 순서를 바꾸므로, 정렬 전 상태 복사를 한번하자.
		List<Apple> inventory2 = new ArrayList<>(inventory);

		// 책에서는 이 부분을 람다 식으로 단순화 할 수 있다는 것을 설명한다.
		Collections.sort(inventory, new Comparator<Apple>() {
			@Override
			public int compare(Apple a1, Apple a2) {
				return a1.getWeight().compareTo(a2.getWeight());
			}
		});

		// 아래 코드가 새로운 Java 8의 기능이라고 하는데...
		// comparing 함수가 구현이 되야하는 것 같다.
		// 추후 배우면 적용하자.
		// inventory2.sort(comparing(Apple::getWeight));

		logger.info("======= 정렬 후 =======");
		inventory.forEach(apple -> logger.info("{}", apple));
	}

	/**
	 * p49 1.2.2 코드 넘겨주기 - Java 8 이전의 방식
	 */
	// 녹색 사과 필터링
	List<Apple> filterGreenApples(List<Apple> inventory) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if ("green".equals(apple.getColor())) {
				result.add(apple);
			}
		}
		return result;
	}

	// 무거운 사과 필터링 (150g 이상)
	List<Apple> filterHeavyApples(List<Apple> inventory) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if (apple.getWeight() >= 150) {
				result.add(apple);
			}
		}
		return result;
	}

	/**
	 * p49 1.2.2 코드 넘겨주기 - Java 8 에서는...
	 * 
	 * 검사 조건 메서드는 Apple 도메인에 내장.
	 * 
	 * Predicate란.. 수학에서 인스로 값을 받아 true나 false로 값을 반환하는 함수.
	 * 
	 */
	// 조건에 맞는 사과 필터링
	static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
		List<Apple> result = new ArrayList<>();

		for (Apple apple : inventory) {
			if (p.test(apple)) {
				result.add(apple);
			}
		}
		return result;
	}

	@Test
	void testFilterApples() {
		List<Apple> inventory = testAppleData();

		// 이전의 방식
		List<Apple> greenAppleList = filterGreenApples(inventory);
		List<Apple> heavyAppleList = filterHeavyApples(inventory);

		// Java 8의 방식, 검사 메서드를 static 메서드로 Apple 도메인 메서드로 넣어야함.
		List<Apple> greenAppleListJava8 = filterApples(inventory, Apple::isGreenApple);
		List<Apple> heavyAppleListJava8 = filterApples(inventory, Apple::isHeavyApple);

		assertEquals(greenAppleListJava8, greenAppleList);
		assertEquals(heavyAppleListJava8, heavyAppleList);

	}

	private List<Apple> testAppleData() {
		List<Apple> inventory = new ArrayList<>();
		inventory.add(Apple.builder().color("green").weight(50).build());
		inventory.add(Apple.builder().color("red").weight(150).build());
		inventory.add(Apple.builder().color("yellow").weight(140).build());
		inventory.add(Apple.builder().color("green").weight(160).build());
		return inventory;
	}

	/**
	 * 사용할 메서드가 단순하다면 람다로 인라인 전달이 나음. 그러나 조금 더 복잡한 수행을 한다면 메서드를 정의하고 메서드 레퍼런스를 정의하는
	 * 것이 낫다.
	 */
	@DisplayName("p51 1.2.3 메서드 전달에서 람다로")
	@Test
	void testLamda() {
		List<Apple> inventory = testAppleData();

		List<Apple> greenAppleList = filterApples(inventory, (Apple a) -> "green".equals(a.getColor()));
		assertEquals("green", greenAppleList.get(0).getColor());

		List<Apple> heavyAppleList = filterApples(inventory, (Apple a) -> a.getWeight() >= 150);
		assertTrue(heavyAppleList.get(0).getWeight() >= 150);

		List<Apple> greenAndHeavyAppleList = filterApples(inventory,
				(Apple a) -> a.getWeight() >= 150 && "green".equals(a.getColor()));
		assertEquals("green", greenAndHeavyAppleList.get(0).getColor());
		assertTrue(greenAndHeavyAppleList.get(0).getWeight() >= 150);

		// 스트림으로 필터링해서 다시 리스트로 모아서 반환
		List<Apple> greenAndHeavyAppleListWithStream = greenAndHeavyAppleList.stream()
				.filter((Apple a) -> a.getWeight() >= 150 && "green".equals(a.getColor())).collect(Collectors.toList());
		assertEquals(greenAndHeavyAppleListWithStream, greenAndHeavyAppleList);
		
		// 병렬 수행
		List<Apple> greenAndHeavyAppleListWithParallelStream = greenAndHeavyAppleList.parallelStream()
				.filter((Apple a) -> a.getWeight() >= 150 && "green".equals(a.getColor())).collect(Collectors.toList());
		assertEquals(greenAndHeavyAppleListWithParallelStream, greenAndHeavyAppleListWithStream);
		

	}

}
