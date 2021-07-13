package org.fp024.j8ia.etc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Scanner의 동작을 테스트 해보기 위해서,
 * System.in에 ByteArrayInputStream을 넣어서
 * 
 * testRun03()이 좀 해깔리는 부분이 있다. nextLine을 왜해줘야하지?
 */
class ScannerTest {
	/**
	 * 테스트시 System.in의 내용을 변경하므로 일단 원래내용을 백업해둠.
	 */
	private final static InputStream ORIGIN_STDIN = System.in;

	/**
	 * 테스트 메서드 하나가 끝날 때마다 원래대로 복구
	 */
	@AfterEach
	void AfterEach() throws IOException {
		System.setIn(ORIGIN_STDIN);
	}

	/**
	 * 사용자가 아래 내용 입력 >>>
	 * 
	 * AB[엔터]
	 * 
	 */
	@Test
	void testRun01() {
		ByteArrayInputStream inputStream = 
				new ByteArrayInputStream(("A" + System.lineSeparator()).getBytes());

		
		assertEquals(3, inputStream.available(), "스캐너가 읽기전 3바이트");
		
		// Scanner 생성
		Scanner scanner = new Scanner(inputStream);

		String wordA = scanner.next();
		
		assertEquals("A", wordA);		
		assertEquals(0, inputStream.available(), "입력스트림에 남은 바이트가 없음");
		assertFalse(scanner.hasNext());

		scanner.close();
	}

	/**
	 * 사용자가 아래 내용 입력 >>>
	 * 
	 * ㅁ[엔터]2[엔터]
	 * 
	 */
	@Test
	void testRun02() {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				("ㅁ" + System.lineSeparator() + "2" + System.lineSeparator()).getBytes());

		// Scanner 생성
		Scanner scanner = new Scanner(inputStream);

		String wordA = scanner.next();
		assertEquals("ㅁ", wordA, "데이터에는 \r\n 이 포함되지 않는다.");

		int ia = scanner.nextInt();

		assertEquals(2, ia);
		scanner.close();
	}

	
	/**
	 * 사용자가 아래 내용 입력 >>>
	 * 
	 * 9.9[엔터]1.1[엔터]테스트1[엔터]테스트2[엔터]
	 */
	@Test
	void testRun03() throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				(   "9.9" + System.lineSeparator() 
				  + "1.1" + System.lineSeparator()
			   	  + "A" + System.lineSeparator()
			   	  + "B" + System.lineSeparator()
				).getBytes());

		
		assertEquals(16, inputStream.available(), "스캐너가 읽기전에 16바이트");

		// Scanner 생성
		Scanner scanner = new Scanner(inputStream);

		double da = scanner.nextDouble();
		assertEquals(9.9, da);

		// 그런데.. 이건 사용자의 입력을 기다린 게 아니고, 입력 스트림에 한번에 입력한 내용이긴 해서...
		assertEquals(0, inputStream.available(), "스케너에서 nextXXX함수 호출시 한번에 읽는 듯.");
		assertEquals(-1, inputStream.read());

		double da1 = scanner.nextDouble();
		assertEquals(1.1, da1);

		scanner.nextLine(); // TODO: 여기서 nextLine을 왜해줘야할까?

		String stn = scanner.nextLine();
		assertEquals("A", stn);

		String stn2 = scanner.nextLine();
		assertEquals("B", stn2);

		scanner.close(); // 스캐너 닫을 때 소스의 입력 스트림도 닫아주는 듯.
		// close() 를 여러번 하면 오류가 나야하는데,
		// ByteArrayStream은 close가 모양만 구현되어있다.
	}
	
}
