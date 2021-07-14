package org.fp024.j8ia.etc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Scanner의 동작을 테스트 해보기 위해서,
 * System.in에 ByteArrayInputStream을 넣어서 테스트
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
	 * Scanner의 nextLine()은 개행까지 입력스트림을 읽어내지만
	 * nextXXX() 계열 메서드들은 개행전까지 입력스트림을 읽는 것 같다.
	 */
	@Test
	void testRun() throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				(   "AAA" + System.lineSeparator() 
				  +  System.lineSeparator()
				  +  System.lineSeparator()
				  + "BBB" + System.lineSeparator()
			   	  + "CCC" + System.lineSeparator()
				).getBytes());
		
		if (System.lineSeparator().length() == 2) {
			assertEquals(19, inputStream.available(), "스캐너 처리전에 개행포함 19바이트.");
		} else {
			assertEquals(14, inputStream.available(), "스캐너가 처리전에 개행 포함 14바이트, Unix환경이면 개행이 \\n이여서 1바이트 크기임.");
		}
		
		Scanner scanner = new Scanner(inputStream);
		
    
		// 
		// | AAA | \r\n | \r\n | \r\n | BBB | \r\n | CCC | \r\n |
		//       △ 
		assertEquals("AAA", scanner.next());
		
		// 
		// | AAA | \r\n | \r\n | \r\n | BBB | \r\n | CCC | \r\n | 
		//              △	
		assertEquals("", scanner.nextLine());

		
		// 
		// | AAA | \r\n | \r\n | \r\n | BBB | \r\n | CCC | \r\n |
		//                     △	
		assertEquals("", scanner.nextLine());
		
	
		// 
		// | AAA | \r\n | \r\n | \r\n | BBB | \r\n | CCC | \r\n | 
		//                            △    
		assertEquals("", scanner.nextLine());
		
	
		// 
		// | AAA | \r\n | \r\n | \r\n | BBB | \r\n | CCC | \r\n | 
		//                                  △
		assertEquals("BBB", scanner.next());
	
		
		// 
		// | AAA | \r\n | \r\n | \r\n | BBB | \r\n | CCC | \r\n |
		//                                         △
		assertEquals("", scanner.nextLine());

		
		// 
		// | AAA | \r\n | \r\n | \r\n | BBB | \r\n | CCC | \r\n |
		//                                               △
		assertEquals("CCC", scanner.next());
		
		// 
		// | AAA | \r\n | \r\n | \r\n | BBB | \r\n | CCC | \r\n |
		//                                                      △ 
		assertEquals("", scanner.nextLine());
		
		scanner.close();
	}
}
