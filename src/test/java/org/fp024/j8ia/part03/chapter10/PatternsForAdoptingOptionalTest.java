package org.fp024.j8ia.part03.chapter10;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.fp024.j8ia.part03.chapter10.IntroducingTheOptionalClass.Car;
import org.fp024.j8ia.part03.chapter10.IntroducingTheOptionalClass.Insurance;
import org.fp024.j8ia.part03.chapter10.IntroducingTheOptionalClass.Person;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

/**
 * 10.3 Optional 적용 패턴
 */
class PatternsForAdoptingOptionalTest {

	/**
	 * 10.3.1 Optional 객체 만들기
	 */
	@Test
	void testCreatingOptionalObjects() {
		// 빈 Optinal
		Optional<Car> optCar = Optional.empty();
		assertFalse(optCar.isPresent());
		
		
		// null 이 아닌 값으로 Optional 만들기
		optCar = Optional.of(new Car());
		assertTrue(optCar.isPresent());
		
		// of()에 null 참조가 들어갈 경우 NPE 발생
		Car nullCar = null;
		assertThrows(NullPointerException.class, () -> Optional.of(nullCar));
		
		// null 값으로 Optional 만들기
		assertFalse(Optional.ofNullable(nullCar).isPresent());	
	}
	
	
	/**
	 * 10.3.2 맵으로 Optional의 값을 추출하고 변환하기
	 */
	@Test
	void testExtractionAndTransformingValuesFromOptionalsWithMap() {
		// 값이 있으면 변경, 없으면 아무일도 안함.
		Insurance insurance = new Insurance("자동차보험");
		
		Optional<String> name = getInsuranceName(insurance);
		assertEquals("자동차보험", name.get());
		
		Insurance nullNameInsurance = new Insurance(null);
		name = getInsuranceName(nullNameInsurance);
		
		assertFalse(name.isPresent());
		
	}

	/**
	 * Optional은 한게 이하인 데이터 컬랙션으로 생각할 수 있음.
	 * 값이 있으면 그값에 대한 Optional 객체 반환 없으면 빈 Optional 객체 반환
	 */
	private Optional<String> getInsuranceName(Insurance insurance) {
		Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
		Optional<String> name = optInsurance.map(Insurance::getName);
		return name;
	}
	
	
	/**
	 * 10.3.3 flatMap으로 Optional 객체 연결
	 */
	@Test
	void testChainingOptionalObjectsWithFlatMap() {
		assertFalse(IntroducingTheOptionalClass.getInsuranceNameUseMap(new Person()).isPresent());
		
		assertEquals("Unknown", IntroducingTheOptionalClassNonNull.getCarInsuranceNameUseFlatMap(new IntroducingTheOptionalClassNonNull.Person(10)));
	}


	
	/**
	 * 도메인 모델에 Optional을 사용했을 때, 데이터를 직렬화 할 수 없는 이유
	 * 		Optional이 Serializable 인터페이스를 구현하지 않음.
	 */
	
	
	/**
	 * 10.3.4 디폴트 액션과 Optional 언랩
	 * 
	 * 	T orElseGet(Supplier<? extends T> other)
	 * 		orElse의 게으른 버전 값이 없을 때만, Supplier 실행
	 * 
	 *  <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X
	 * 		Optional이 비었을 때, 예외 발생
	 * 
	 *  void ifPresent(Consumer<? super T> consumer)
	 *  	값이 존재할 때, 인수로 넘겨준 동작 실행, 없으면 아무일도 안함.
	 * 
	 */

	/**
	 * 10.3.5 두 Optional 합치기
	 */
	@Test
	void testQuizMeargeTwoOptionals() {
		Optional<Person> optPerson = Optional.of(new Person());
		Optional<Car> optCar = Optional.of(new Car());
		
		Optional<Insurance> expect = nullSafeFindCheapestInsuranceSolutionBook(optPerson, optCar);
		Optional<Insurance> actual = nullSafeFindCheapestInsuranceSolution(optPerson, optCar);

		// Optional의 경우 리플렉션 Equals 비교가 안되는 것 같다.
		// 내부 값을 얻어서 비교하자.
		assertTrue((new ReflectionEquals(expect.get())).matches(actual.get()));
	}
	
	// 내가한 것 보다 저자님이한게 난 것 같다 .. 내가한건
	// get을 실행시키는 시점에.. person과 car가 확실히 존재하고 있음을 검증할 수 있긴하지만..
	// get()을 노출시키는게 좀 그렇다. ㅠㅠ
	// 뭔가 어거지로 들어맞은 것 같은... 전달 받은 car가 Optional<Car>가 아니라면 flatMap의 리턴으로 넣을 수도 없었다.
	Optional<Insurance> nullSafeFindCheapestInsuranceSolution(Optional<Person> person, Optional<Car> car) {
		//return person.flatMap(p -> car).map(c -> findCheapestInsurance(person.get(), car.get()));
		return person.flatMap(p -> car).map(c -> findCheapestInsurance(person.get(), car.get()));
	}

	// 저자님 정답
	Optional<Insurance> nullSafeFindCheapestInsuranceSolutionBook(Optional<Person> person, Optional<Car> car) {
		return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
	}
	
	Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car) {
		if (person.isPresent() && car.isPresent()) {
			return Optional.of(findCheapestInsurance(person.get(), car.get()));
		} else {
			return Optional.empty();
		}
	}

	private Insurance findCheapestInsurance(Person person, Car car) {
		// 뭔가 수행...
		return new Insurance("퀴즈10-1보험");
	}
	
	
	/**
	 * 10.3.6 필터로 특정값 거르기
	 * 
	 * Optional도 filter 메서드를 사용할 수 있음
	 * 본문의 예제가 그렇게 어렵지 않아서 퀴즈 바로 진행하자. 
	 * 
	 * 퀴즈 10-2 Optional 필터링
	 */
	@Test
	void testQuizOptionalFiltering() {		
		IntroducingTheOptionalClassNonNull.Person p = new IntroducingTheOptionalClassNonNull.Person(18);
		IntroducingTheOptionalClassNonNull.Car c = new IntroducingTheOptionalClassNonNull.Car();
		p.setCar(c);
		c.setInsurance(new IntroducingTheOptionalClassNonNull.Insurance("퀴즈10-2보험"));
		
		assertEquals("퀴즈10-2보험", IntroducingTheOptionalClassNonNull.getCarInsuranceNameUseFlatMapWithAgeFilter(p, 18));
		
		assertEquals("Unknown", IntroducingTheOptionalClassNonNull.getCarInsuranceNameUseFlatMapWithAgeFilter(p, 19));
	}
}
