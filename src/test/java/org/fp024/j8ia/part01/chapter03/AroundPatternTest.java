package org.fp024.j8ia.part01.chapter03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * 점점 어려워지고 있다.. 그런데.. 활용을 자유롭게 할 수 있다면 좋을 것 같다.
 * 기존 예제는 리소스에 txt를 만들어둔것 같은데.. @TempDir 어노테이션으로 임시폴더에다 임시파일을 만든 식으로 테스트 했다.
 */
class AroundPatternTest {
	/** 테스트 클래스 실행이 완료되면 임시디렉토리는 지워진다. */
	@TempDir
	static Path sharedTempDir;

	@BeforeAll
	static void beforeAll() throws IOException {
		Path dataTxtFile = sharedTempDir.resolve("data.txt");
		List<String> lines = Arrays.asList("Java", "8", "Lambdas", "In", "Action");
		Files.write(dataTxtFile, lines);
	}

	@Test
	void testLegacy() throws IOException {
		assertEquals("Java", processFile());
	}

	static String processFile() throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(sharedTempDir.toFile(), "data.txt")))) {
			return br.readLine();
		}
	}

	@Test
	void testNew() throws IOException {
		assertEquals("Java", processFile((BufferedReader b) -> b.readLine()));
		assertEquals("Java8", processFile((BufferedReader b) -> b.readLine() + b.readLine()));
	}

	/**
	 * BufferedReader 를 전달받아 내부 구현을 전달 받는 함수형 인터페이스?
	 */
	@FunctionalInterface
	interface BufferedReaderProcessor {
		String process(BufferedReader b) throws IOException;
	}

	/**
	 * Buffered는 실행 메서드에서 생성한 것을 사용하고, process의 내부 구현은 호출처에서 전달해준 것으로 사용했다.
	 */
	static String processFile(BufferedReaderProcessor p) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(sharedTempDir.toFile(), "data.txt")))) {
			return p.process(br);
		}
	}

}
