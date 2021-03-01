package org.fp024.j8ia.part02.chapter05;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 5.5 실전 연습
 */
@Slf4j
class PuttingItAllIntoPracticeTest {
	private List<Transaction> transactions = TransactionRepository.getTransaction();
	
	/**
	 * 1. 2011년에 일어난 모든 트랜젝션을 찾아 값을 오름 차순으로 정리하시오.
	 */
	@Test
	void testQuiz01() {
		transactions.stream()
			.filter(t -> t.getYear() == 2011)
			.sorted(Comparator.comparing(Transaction::getValue))
			.forEach(t -> {
				logger.info(t.toString());
				assertEquals(2011, t.getYear());
			});
	}

	
	/**
	 * 2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
	 */
	@Test
	void testQuiz02() {
		List<String> actual = transactions.stream()
			.map(t -> t.getTrader().getCity())
			.distinct()
			.collect(toList());
		assertEquals(Arrays.asList("Cambridge", "Milan"), actual);		
	}

	
	/**
	 * 3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오
	 * 
	 * 보통 unix 명령 쓸 때도 uniq 보다 sort를 먼저했었으니... distinct를 나중에하자.
	 */
	@Test
	void testQuiz03() {
		 List<String> actual = transactions.stream()
			.filter(t -> t.getTrader().getCity() == "Cambridge")
			.map(t -> t.getTrader().getName())
			.sorted(Comparator.comparing(String::toString))
			.distinct()
			.collect(toList());
		
		 assertEquals(Arrays.asList("Alan", "Brian", "Raoul"), actual);
	}

	
	@Test
	void testQuiz03Book() {
		 List<Trader> actual = transactions.stream()
			.filter(t -> t.getTrader().getCity() == "Cambridge")
			.map(t -> t.getTrader())
			.distinct()
			.sorted(Comparator.comparing(Trader::getName))
			.collect(toList());
		
		 // 객체 참조가 완전히 동일해서 Equals를 오버라이드 하지 않아도 비교가 되긴했다.
		 assertEquals(Arrays.asList(
				   TraderRepository.getTrader("alan")
				 , TraderRepository.getTrader("brian")
				 , TraderRepository.getTrader("raoul")), actual);
		
	}
	
	
	/**
	 * 4. 모든 거래자의 이름을 알파벳 순으로 정렬해서 반환하시오.
	 */
	@Test
	void testQuiz04() {
		List<String> actual = transactions.stream()
			.map(t -> t.getTrader().getName())
			.sorted() // String에 대해서 자연율이라면 인자안넣어도될것 같다. , 그런데 저자님은 고유한 걸 먼저 처리한다.
			.distinct()
			.collect(toList());
			
		assertEquals(Arrays.asList("Alan", "Brian", "Mario", "Raoul"), actual);
	}

	/**
	 * 4번문제의 의미가 다 연결하라는 의미인줄은 몰랐다.
	 */
	@Test
	void testQuiz04Book() {
		String actual = transactions.stream()
			.map(t -> t.getTrader().getName())
			.distinct()
			.sorted() 			
			.reduce("", (n1, n2) -> n1 + n2);
			
		assertEquals("AlanBrianMarioRaoul", actual);
	}
	
	
	/**
	 * 5. 밀라노에 거래자가 있는가? findAny
	 */
	@Test
	void testQuiz05() {
		Optional<Transaction> actual = transactions.stream()
			.filter(t -> "Milan".equals(t.getTrader().getCity()))
			.findAny();

		assertTrue(actual.isPresent());
		assertEquals("Milan", actual.get().getTrader().getCity());
	}

	
	/**
	 * 5. 그냥 anyMatch를 쓰면 되는구나, 나는 필터링한다음 findAny로 찾았는데...
	 */
	@Test
	void testQuiz05Book() {
		boolean actual = transactions.stream()
			.anyMatch(t -> "Milan".equals(t.getTrader().getCity()));

		assertTrue(actual);		
	}	
	
	
	/**
	 * 6. 케임브리지에 거주하는 거래자의 모든 트랜젝션 값을 출력하시오.
	 */
	@Test
	void testQuiz06() {
		transactions.stream()
			.filter(t -> t.getTrader().getCity() == "Cambridge")
			.map(Transaction::getValue)
			.forEach(t -> logger.info(t.toString()));
	}

	/**
	 * 7. 전체 트랜젝션 중 최댓값은 얼마인가
	 */
	@Test
	void testQuiz07() {
		Optional<Transaction> actual = transactions.stream()
				.max(Comparator.comparing(Transaction::getValue));
		
		assertTrue(actual.isPresent());
		assertEquals(1000, actual.get().getValue());		
	}
	
	/**
	 * 8. 전체 트랜젝션 중 최솟값은 얼마인가
	 */
	@Test
	void testQuiz08() {
		Optional<Transaction> actual = transactions.stream()
				.min(Comparator.comparing(Transaction::getValue));
		
		assertTrue(actual.isPresent());
		assertEquals(300, actual.get().getValue());				
	}
	
	/**
	 * 8. 저자님은 reduce 설명을 위해 reduce로 최소값을 구한 것 같다.
	 */
	@Test
	void testQuiz08Book() {
		Optional<Transaction> actual = transactions.stream()
				.reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2 );
		
		assertTrue(actual.isPresent());
		assertEquals(300, actual.get().getValue());				
	}
	
	
}
