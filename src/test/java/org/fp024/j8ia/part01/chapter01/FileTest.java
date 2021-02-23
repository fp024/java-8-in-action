package org.fp024.j8ia.part01.chapter01;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.File;
import java.io.FileFilter;

import org.junit.jupiter.api.Test;

/**
 * p47. 메서드 레퍼런스
 * 
 * 그동안 객체를 만들어 객체 참조로 전달해왔는데, Java 8에서는 메서드 참조문법으로 전달 가능.
 */
class FileTest {
	@Test
	void fileTest() {
		// 숨겨진 파일 필터링
		File[] hiddenFiles = new File(".").listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isHidden();
			}
		});

		// Java 8의 메서드 레퍼런스
		File[] hiddenFilesNew = new File(".").listFiles(File::isHidden);

		// eclipse에서 JUnit5로 실행시, .\.git 을 숨김 파일로 인식.
		assertArrayEquals(hiddenFilesNew, hiddenFiles);
	}

}
