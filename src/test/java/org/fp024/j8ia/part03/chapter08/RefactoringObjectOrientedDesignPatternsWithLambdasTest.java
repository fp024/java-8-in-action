package org.fp024.j8ia.part03.chapter08;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 8.2 람다로 객체향 디자인 패턴 리펙토링하기
 */
@Slf4j
class RefactoringObjectOrientedDesignPatternsWithLambdasTest {
	/**
	 * 8.2.1 전략
	 */
	@Test
	void testStrategy() {
		Validator numericValidator = new Validator(new IsNumeric());

		assertFalse(numericValidator.validate("abcde"));
		assertTrue(numericValidator.validate("12345"));

		Validator lowerCaseValidator = new Validator(new IsAllLowerCase());

		assertTrue(lowerCaseValidator.validate("abcde"));
		assertFalse(lowerCaseValidator.validate("12345"));
	}

	/**
	 * 람다 사용시 좋은 점이... 구현 클래스 파일을 만들지 않아도 바로 적용할 수 있어 좋아보임.
	 */
	@Test
	void testStrategyWithLambda() {
		Validator numericValidator = new Validator(s -> s.matches("\\d+"));

		assertFalse(numericValidator.validate("abcde"));
		assertTrue(numericValidator.validate("12345"));

		Validator lowerCaseValidator = new Validator(s -> s.matches("[a-z]+"));

		assertTrue(lowerCaseValidator.validate("abcde"));
		assertFalse(lowerCaseValidator.validate("12345"));
	}

	/**
	 * 8.2.2 템플릿 메서드
	 */
	@Test
	void testTemplateMethod() {
		new OnlineBanking() {
			@Override
			void makeCustomerHappy(Customer c) {
				logger.info("안녕하세요 {}님 좋은아침입니다.!!ㅎㅎ", c.getName());
			}
		}.processCustomer(1);
	}

	@Test
	void testTemplateMethodWithLambda() {
		new OnlineBankingLambda().processCustomer(3, c -> logger.info("안녕하세요 {}님 좋은아침입니다.!!ㅎㅎ", c.getName()));
	}

	/**
	 * 8.2.3 옵저버
	 */
	@Test
	void testObserver() {
		Feed f = new Feed();
		f.registerObserver(new NYTimes());
		f.registerObserver(new Guardian());
		f.registerObserver(new LeMonde());
		f.notifyObervers("The queen said her favorite book is Java 8 in Action!");
	}

	/**
	 * 간단하면 람다표현식을 인라인으로 써도 되겠지만 
	 * 복잡한 로직이 들어가면 별도 클래스 작성이 나을 수 있음.
	 */
	@Test
	void testObserverWithLambdas() {
		Feed f = new Feed();
		f.registerObserver((String tweet) -> {
			if (tweet != null && tweet.contains("money")) {
				logger.info("Breaking news in NY! {}", tweet);
			}
		});
		f.registerObserver((String tweet) -> {
			if (tweet != null && tweet.contains("queen")) {
				logger.info("Yet another news in London... {}", tweet);
			}
		});
		f.registerObserver((String tweet) -> {
			if (tweet != null && tweet.contains("wine")) {
				logger.info("Today cheese, wine and news! {}", tweet);
			}
		});
		f.notifyObervers("The queen said her favorite book is Java 8 in Action!");
	}

	
	/**
	 * 8.2.4 의무 체인
	 */
	@Test
	void testChainOfResponsibility() {
		ProcessingObject<String> p1 = new HeaderTextProcessing();
		ProcessingObject<String> p2 = new SpellCheckerProcessing();
		
		// 두 작업 처리 객체 연결
		p1.setSuccessor(p2);
		
		String result = p1.handle("Aren't labdas really sexy?");
		
		assertEquals("From Raoul, Mario and Alan: Aren't lambdas really sexy?" , result);
	}

	@Test
	void testChainOfResponsibilityWithLambdas() {
		
		// Function<T,T> : 입력 타입과 결과 타입이 같은 함수
		UnaryOperator<String> headerProcessing = (String input) ->  "From Raoul, Mario and Alan: " + input;
		
		UnaryOperator<String> spellCheckerProcessing = (String input) ->  input.replace("labda", "lambda");
		
		Function<String, String> pipeline = headerProcessing.andThen(spellCheckerProcessing);
		
		assertEquals("From Raoul, Mario and Alan: Aren't lambdas really sexy?",
				// 동작 체인으로 두 함수 조합.
				pipeline.apply("Aren't labdas really sexy?"));
	}
	
	/**
	 * 8.2.5 팩토리
	 */
	@Test
	void testFactory() {
		Product p = ProductFactory.createProduct("loan");
		assertEquals("대출", p.getName());
		
		Product p2 = ProductFactoryLambdas.createProduct("stock");
		assertEquals("주식", p2.getName());
	}

}
