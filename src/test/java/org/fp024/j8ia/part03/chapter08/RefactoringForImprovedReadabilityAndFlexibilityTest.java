package org.fp024.j8ia.part03.chapter08;


import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.fp024.j8ia.part02.chapter05.Dish;
import org.fp024.j8ia.part02.chapter05.DishRepository;
import org.fp024.j8ia.part02.chapter06.CaloricLevel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 8.1 가독성과 유연성을 개선하는 리팩토링
 */
@Slf4j
class RefactoringForImprovedReadabilityAndFlexibilityTest {
	@BeforeAll
	static void beforeAll() {
		
	}
	
	
	private final List<Dish> menu = DishRepository.getDishList(); 

	/**
	 * 8.1.2 익명 클래스를 람다 표현식으로 리펙토링하기
	 */
	@Test
	void testFromAnonymousClassesToLambdaExpressions() {
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				logger.info("hello");
			}
		};
		r1.run();
		
		Runnable r2 = () -> logger.info("hello");
		r2.run();
	
		// 람다 표현식의 로컬 변수는감싸진 범위에 정의된 로컬 변수를 재정의할 수 없음.
		// Lambda expression's local variable a cannot redeclare another local variable defined in an enclosing scope.
		/*
		int a = 10;
		Runnable r3 = () -> {
			int a= 2; // 컴파일 오류
			logger.info("{}", a);
		};
		*/
		
		// 그러나 익명 클래스는 재정의 할 수 있다. (Shadow variable)
		int a = 10;
		Runnable r3 = new Runnable() {

			@Override
			public void run() {
				int a = 2; // 정상 실행
				logger.info("{}", a);
			}
		};
		
		
		
	}

	/**
	 * 람다 사용시 모호한 호출 처리
	 */
	@Test
	void testAmbiguousMethodCall() {
		// 익명 클래스 사용: 호출에 모호함이 없음.
		doSomething(new Task() {			
			@Override
			public void execute() {
				logger.info("Danger danger!! - anonymous");
			}
		});
		
		// doSomething(Runnable r) 를 호출해야할지 doSomething(Task a) 를 호출해야할지 모호함.
		// The method doSomething(Runnable) is ambiguous for the type RefactoringForImprovedReadabilityAndFlexibilityTest
		// doSomething(() -> logger.info("Danger danger"));
		
		// 명시적 캐스팅을 붙여서 해결가능.
		doSomething((Task)() -> logger.info("Danger danger!! - lambda"));
		
	}

	static void doSomething(Runnable r) {
		r.run();
	}
	
	static void doSomething(Task a) {
		a.execute();
	}
	
	
	/**
	 * 8.1.3 람다 표현식을 메서드 레퍼런스로 리펙토링하기
	 * 
	 * 요리 데이터는 5장, 레벨 타입은 6장 패키지 사용.
	 */
	@Test
	void testFromLambdaExpressionsToMethodReferences() {
		Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream()
			.collect(groupingBy(dish -> { // 이 메서드 블럭 부분을 Dish의 도메인 메서드로 넣으면 메서드 레퍼런스를 활용할 수 있음.
					if (dish.getCalories() <= 400) {
						return CaloricLevel.DIET;
					} else if (dish.getCalories() <= 700) {
						return CaloricLevel.NORMAL;
					} else {
						return CaloricLevel.FAT;
					}
			}));
		
		Map<CaloricLevel, List<Dish>> dishesByCaloricLevelUseRef = menu.stream()
				.collect(groupingBy(Dish::getCaloricLevel)); // 도메인에 칼로리 레벨을 얻는 메서드를 넣고 메서드 레퍼런스 호출방식으로 전환
		
		assertEquals(dishesByCaloricLevel, dishesByCaloricLevelUseRef);
	}
	
	/**
	 * 컬렉터의 내장 헬퍼 메서드 활용
	 */
	@Test
	void testCollectorsHelperMethod() {
		Integer useReduce = menu.stream().map(Dish::getCalories).reduce(0, (c1, c2) -> c1 + c2);
		
		int useIntStream = menu.stream().mapToInt(Dish::getCalories).sum();
		
		// summingInt 가 Collectors의 정적 메서드다
		Integer summingInt = menu.stream().collect(summingInt(Dish::getCalories));

		assertEquals(useReduce,useIntStream);
		assertEquals(useReduce,summingInt);		
	}
	
	
	/**
	 * 8.1.4 명령형 데이터 처리를 스트림으로 리팩토링하기
	 */
	@Test
	void testFromImperativeDataProcessingToStreams() {
		List<String> dishNamesUseLegacy = new ArrayList<String>();		
		for (Dish dish : menu) {
			if (dish.getCalories() > 300) {
				dishNamesUseLegacy.add(dish.getName());
			}
		}
		
		List<String> dishNamesUseStream = menu.parallelStream()
					.filter(d -> d.getCalories() > 300)
					.map(Dish::getName)
					.collect(toList());
		
		assertIterableEquals(dishNamesUseLegacy, dishNamesUseStream);
	
	}
	
	
	/**
	 * 8.1.5 코드 유연성 개선
	 * 
	 * 책에 FINER 로그 레벨의 내용을 출력하는 예제가 있는데,
	 * JRE/lib/logging.properties 에 Console 로그 레벨이 INFO여서 테스트를 확인을 할 수 가 없어서,
	 * 실행시점에 콘솔 로그에 접근해서 레벨을 FINER로 바꾸는 식으로 수정. 
	 * 
	 * JUnit 실행시킬 때, VM 옵션으로 프로퍼티에서 직접 실행시키거나
	 * logging.properties 파일을 src/test/resources에 위치하고, 아래처럼 
	 * 시스템 프로퍼티를 설정해서 수행해볼 수도 있다고 하는데, 일단 아래내용은 안됨.
	 * System.setProperty("java.util.logging.config.file", ClassLoader.getSystemResource("logging.properties").getPath());
	 *  
	 * 실제 결과확인은 실행시점에 로그레벨 바꿔서 실행 것이 정상동작함.
	 * 프로퍼티로 처리하는 방법은 다시 나중에 해보자..
	 * 
	 * 
	 * https://stackoverflow.com/questions/38566865/how-to-set-the-loglevel-for-a-junit-test/43722641#43722641
	 * 
	 */
	@Test
	void testImprovingCodeFlexibility() {
		// Java 기본 로거
		Logger jLogger = Logger.getLogger(getClass().getName());
		jLogger.setLevel(Level.FINER);

		ConsoleHandler handler = getConsoleHandler(jLogger.getParent().getHandlers());

		logger.info("수정전 JRE기본 Console 로그레벨: {}", handler.getLevel());
		handler.setLevel(Level.FINER);

		if (jLogger.isLoggable(Level.FINER)) {
			jLogger.finer("Problem: " + generateDiagnostic());
		}
		
		// 저자님은 아래 내용이 바람직하다고 하는데...
		// 위의 .finer()메서드도 내부에 로깅 레벨 체크 로직이 들어있다.
		// 뭔가 이유가 있을 것 같은데...
		// 레벨을 FINER 보다 더 낮은 FINEST로 더 낮추고 중단점을 찍어봤을 때.. 
		// 아래 메서드도 generateDiagnostic 메서드 호출은 일어난다.
		jLogger.log(Level.FINEST, "Problem: " + generateDiagnostic());
		

		// 그러나 다음 저자님이 언급한대로 람다식으로 바꾸면 람다식 블럭 부분 실행 자체가 안일어남.
		// 확실히 장점 같다.
		jLogger.log(Level.FINEST, () -> "Problem: " + generateDiagnostic());
		
		// AdoptJDK 1.8_285 - java.util.logging.Logger.log() 구현 
		/* 
	 		public void log(Level level, Supplier<String> msgSupplier) {
		        if (!isLoggable(level)) { // 로그레벨이 안맞으면 바로 리턴해서 서플라이의 블록이 실행되지 않음.
		            return;
		        }
		        LogRecord lr = new LogRecord(level, msgSupplier.get());
		        doLog(lr);
	        }
        */
    }

	/**
	 * 헨들러 배열을 받아서, 콘솔 핸들러를 찾는대로 바로 반환 (근데 하나밖에 없다.)
	 * 전혀 못찾을 경우 예외 발생
	 */
	private ConsoleHandler getConsoleHandler(Handler[] handlers) {
		return (ConsoleHandler) Stream.of(handlers)
				.filter(h -> h.getClass() == ConsoleHandler.class)
				.findAny()
				.orElseThrow(IllegalStateException::new);
	}

	String generateDiagnostic() {
		return "run generateDiagnostic";
	}
	
	
	
	/**
	 * 실행 어라운드 
	 */
	@Test
	void testExecuteAround() throws IOException {
		String oneLine = processFile((BufferedReader b) -> b.readLine());
		
		assertEquals("The quick brown fox jumped over the lazy dog", oneLine);
		
		String twoLines = processFile((BufferedReader b) -> b.readLine() + b.readLine());

		assertEquals("The quick brown fox jumped over the lazy dogThe lazy dog jumped over the quick brown fox", twoLines);
	}
	
	static String processFile(BufferedReaderProcessor p) throws IOException {
		try (BufferedReader br = new BufferedReader(
				new FileReader(ClassLoader.getSystemResource("chapter05/data.txt").getPath()))) {
			return p.process(br);
		}
	}

	interface BufferedReaderProcessor {
		String process(BufferedReader b) throws IOException;
	}
	
	
	
	
	
	
	
}
