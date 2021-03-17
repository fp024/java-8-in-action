package org.fp024.j8ia.part03.chapter10;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 10.2 Optional 클래스 소개
 * 
 * 		코드 상으로 Optional<Car> car
 *  	Optional<Insurance> insurance 이 null이 되는 상황이 만들어진다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class IntroducingTheOptionalClass {

	static class Person {		
		private Optional<Car> car;

		public Optional<Car> getCar() {
			return car;
		}
	}

	static class Car {
		private Optional<Insurance> insurance;

		public Optional<Insurance> getInsurance() {
			return insurance;
		}
	}

	@RequiredArgsConstructor
	static class Insurance {
		private final String name;

		public String getName() {
			return name;
		}
	}
	
	/*// Optional로 처리되서 메서드 레퍼런스를 쓸수 없음 Optional로 풀어써야한다.
	private Optional<String> getInsuranceName(Person person) {
		Optional<Person> optPerson = Optional.of(person);
		return optPerson.map(Person::getCar)
				// The type IntroducingTheOptionalClass.Car does not define 
				// getInsurance(Optional<IntroducingTheOptionalClass.Car>) that is applicable here
				.map(Car::getInsurance)
				.map(Insurance::getName);
	}
	*/

	// Optional로 풀어 수행.
	static Optional<String> getInsuranceNameUseMap(Person person) {
		Optional<Person> optPerson = Optional.of(person);
		return optPerson.map(Person::getCar)
				// 위에 까진 수행이 잘 되는데.. getCar() 수행시, 
				// Optional.ofNullable(null) 로 반환되어, 다음 맵에서, 다음 맵이 수행되지 않고 끝남.  
				.map((Optional<Car> c) -> c.get().getInsurance())
				.map((Optional<Insurance> i) -> i.get().getName());
	}	
}

/**
 * Optional 결과가 항상 null이 될 수 없게 수정.
 * 
 * Optional 참조 자체가 null이 되는 상황이 되면, Optional의 장점이 무효화됨.
 * https://stackoverflow.com/questions/53679130/optional-flatmap-in-java-has-nullpointerexception
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class IntroducingTheOptionalClassNonNull {
	
	@RequiredArgsConstructor
	static class Person {
		// 퀴즈 10-2 때문에 추가
		@Getter
		private final int age;
		
		@Setter
		private Car car;

		public Optional<Car> getCar() {
			return Optional.ofNullable(car);
		}
	}

	static class Car {
		@Setter
		private Insurance insurance;

		public Optional<Insurance> getInsurance() {
			return Optional.ofNullable(insurance);
		}
	}

	@RequiredArgsConstructor
	static class Insurance {
		private final String name;

		public String getName() {
			return name;
		}
	}
	
	
	/**
	 * Optional map의 반환 부분
	 * 		return Optional.ofNullable(mapper.apply(value));
	 * 
	 * Optional flatMap의 반환 부분
	 * 		return Objects.requireNonNull(mapper.apply(value));
	 * 
	 * Optional의 map을 쓸 때는 내부 Map이 반환하는 변환 값이 null 이 아니여도,
	 * 알아서 Optional.empty()가 최종실행되어 문제가 없으나...
	 * 
	 * flatMap을 사용할 때는 AdoptOpenJDK 코드상으로 
	 * 반환 값이  Null이 아님을 요구합니다.
	 * 
	 */
	static String getCarInsuranceNameUseFlatMap(Person person) {
		Optional<Person> optPerson = Optional.of(person);
		return optPerson
			.flatMap(Person::getCar)
			.flatMap(Car::getInsurance)
			.map(Insurance::getName)
			.orElse("Unknown");
	}	
	
	// 퀴즈 10-2:  Optional 필터링
	static String getCarInsuranceNameUseFlatMapWithAgeFilter(Person person, int minAge) {
		Optional<Person> optPerson = Optional.of(person);
		return optPerson
			.filter(p -> p.getAge() >= minAge)
			.flatMap(Person::getCar)
			.flatMap(Car::getInsurance)
			.map(Insurance::getName)
			.orElse("Unknown");
	}
}
