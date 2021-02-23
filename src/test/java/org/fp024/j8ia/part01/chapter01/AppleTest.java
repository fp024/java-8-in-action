package org.fp024.j8ia.part01.chapter01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class AppleTest {
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

}
