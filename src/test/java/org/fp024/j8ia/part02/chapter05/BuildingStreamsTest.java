package org.fp024.j8ia.part02.chapter05;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 5.7 스트림 만들기
 */
@Slf4j
class BuildingStreamsTest {	
	/**
	 * 5.7.1 값으로 스트림 만들기
	 */
	@Test
	void testStreamFromValues() {
		Stream<String> stream = Stream.of("Java 8", "Lambdas", "In", "Actions");
		
		assertIterableEquals(Arrays.asList("JAVA 8", "LAMBDAS", "IN", "ACTIONS")
				, stream.map(String::toUpperCase).collect(Collectors.toList()));

	}
	
	/**
	 * 5.7.2 배열로 스트림 만들기
	 */
	@Test
	void testStreamFromArrays() {
		int[] numbers = { 2, 3, 5, 7, 11, 13 };
		assertEquals(41, Arrays.stream(numbers).sum());
	}
	
	/**
	 * 5.7.3 파일로 스트림 만들기
	 */
	@Test
	void testStreamFromFiles() {
		long uniqueWords = 0;
		
		// JUnit에서 로드할 때는 전체경로를 다 써줘야했음..
		// https://www.baeldung.com/junit-src-test-resources-directory-path
		try (Stream<String> lines = Files.lines(Paths.get("src", "main", "resources", "chapter05", "data.txt"), Charset.defaultCharset())) {
			uniqueWords = lines.flatMap(line -> 
										Arrays.stream(line.split(" ")) 
						 ) // [The, quick, brown, fox, jumped, over, the, lazy, dog], [The, lazy, dog, jumped, over, the, quick, brown, fox] 
						   // 2줄(2개의 문자열배열)이 flatMap에 의해 하나의 스트림으로 만들어짐.
						.map(String::toLowerCase) // 이걸 빼면 소문자로 시작하는 the 때문에 9개가된다.
						.distinct()
						.count();
			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			fail();
		}

		assertEquals(8, uniqueWords);
	}
	
	
	/**
	 * 5.7.4 함수로 무한 스트림 만들기 - iterate
	 */
	@Test
	void testCreatingInfiniteStreamsIterate() {
		// iterate 
		Stream.iterate(0, n -> n + 2)
				.limit(10)
				.forEach(i -> logger.info("{}", i));
	}
	
	
	/**
	 * Quiz 5-4 피보나치 수열 집합
	 * (0,1), (1,1), (1,2), (2,3), (3, 5), (5, 8), ... 이런 피보나치 수열 집합을 만드는게 목표
	 * 
	 * ==> 답을 봐버렸다. ㅠㅠ
	 */
	@Test
	void testQuizFibonacciTuples() {
		Stream.iterate(new int[] { 0, 1 }, 
				t -> new int[] { t[1], t[0] + t[1] })
				.limit(20)
				.forEach(t -> logger.info("({},{})", t[0], t[1]));
	}

	
	/**
	 * 5.7.4 함수로 무한 스트림 만들기 - generate
	 */
	@Test
	void testCreatingInfiniteStreamsGenerate() {
		Stream.generate(Math::random)
				.limit(5)
				.forEach(t -> logger.info("{}", t));
			
		
		IntStream ones = IntStream.generate(()->1);
		assertArrayEquals(new int[] { 1, 1, 1, 1, 1 }, ones.limit(5).toArray());
	}
	
	
	/**
	 * IntSupplier를 구현하고, generate를 사용하여 피보니치 구현
	 * 
	 * 그런데.. IntSupplier를 상태를 가지게 만들었다.
	 */
	@Test
	void testIntSupplier() {
		IntSupplier fib = new IntSupplier() {			
			private int previous = 0;
			private int current = 1;
			
			@Override
			public int getAsInt() {
				int oldPrevious = this.previous;
				int nextValue = this.previous + this.current;
				this.previous = this.current;
				this.current = nextValue;
				return oldPrevious;
			}
		};
		
		IntStream.generate(fib).limit(10).forEach(i -> logger.info("{}", i));
		
		// 1 -> p:1 , c:1 , o:0
		// 2 -> p:1 , c:2 , o:1
		// 3 -> p:2 , c:3 , o:1
		// 4 -> p:3 , c:5 , o:2
		// 5 -> p:5 , c:8 , o:3
		// 6 -> p:8 , c:13 , o:5
		// ... IntSupplier 인스턴스 하나의 상태를 계속 바꾸면서 계산한다.
	}
}
