package org.fp024.j8ia.part03.chapter10;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 10.1 값이 없는 상황을 어떻게 처리할까.
 * 
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class HowDoYouModelTheAbsenceOfAValue {
	static class Person {
		private Car car;

		public Car getCar() {
			return car;
		}
	}

	static class Car {
		private Insurance insurance;

		public Insurance getInsurance() {
			return insurance;
		}
	}

	static class Insurance {
		private String name;

		public String getName() {
			return name;
		}
	}

}
