package org.fp024.j8ia.part03.chapter10;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.fp024.j8ia.part03.chapter10.HowDoYouModelTheAbsenceOfAValue.Car;
import org.fp024.j8ia.part03.chapter10.HowDoYouModelTheAbsenceOfAValue.Insurance;
import org.fp024.j8ia.part03.chapter10.HowDoYouModelTheAbsenceOfAValue.Person;
import org.junit.jupiter.api.Test;

/**
 * 10. null 대신 Optional
 */
class UsingOptionalAsABetterAlternativeToNullTest {

	/**
	 * 10.1 값이 없는 상황을 어떻게 처리할까?
	 */
	@Test
	void testHowDoYouModelTheAbsenceOfAValue() {
		Person person = new Person();
		assertThrows(NullPointerException.class, () -> getCarInsuranceName(person));
	}
	
	String getCarInsuranceName(Person person) {
		return person.getCar().getInsurance().getName();
	}
	
	/**
	 * 10.1 보수 적인 자세로 NPE 줄이기
	 */
	@Test
	void testReducingNullPointerExceptionsWithDefensiveChecking() {
		Person person = new Person();
		assertEquals("Unknown", getCarInsuranceNameWithDefensiveChecking1(person));
		
		assertEquals("Unknown", getCarInsuranceNameWithDefensiveChecking2(person));
	}
	
	// null 안전 시도1: 깊은 의심
	String getCarInsuranceNameWithDefensiveChecking1(Person person) {
		if (person != null) {
			Car car = person.getCar();
			if (car != null) {
				Insurance insurance = car.getInsurance();
				if (insurance != null) {
					return insurance.getName();
				}
			}
		}
		return "Unknown";
	}
	
	// null 안전 시도2: 너무 많은 출구
	String getCarInsuranceNameWithDefensiveChecking2(Person person) {
		final String unknown = "Unknown";
		if (person == null) {
			return unknown;
		}

		Car car = person.getCar();
		if (car == null) {
			return unknown;
		}

		Insurance insurance = car.getInsurance();
		if (insurance == null) {
			return unknown;
		}
		return insurance.getName();
	}
	
	
	/**
	 * 10.1.2 null 때문에 발생하는 문제
	 * 
	 * 	1. 에러의 근원
	 * 
	 *  2. 코드를 어지럽힘.
	 *  
	 *  3. 아무 의미도 없음.
	 *  
	 *  4. 자바 철학에 위배
	 *  	자바는 개발자로 부터 모든 포인터를 숨겼지만 null은 예외.
	 *  
	 *  5. 형식 시스템에 구멍을 만듬
	 *     null은 무형식이며 정보를 포함하지 않음.
	 *     모든 레퍼런스에 할당 가능
	 *     이런 식으로 null이 시스템의 다른 부분으로 퍼졌을 때 null이 어떤으미로 사용되었는지 알 수 없음.
	 */
	
}
