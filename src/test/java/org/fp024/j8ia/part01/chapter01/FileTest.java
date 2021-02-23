package org.fp024.j8ia.part01.chapter01;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.File;
import java.io.FileFilter;

import org.junit.jupiter.api.Test;

/**
 * p47. 메서드 레퍼런스
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

		// .\.git 을 숨김 파일로 인식.
		assertArrayEquals(hiddenFilesNew, hiddenFiles);
	}

}
