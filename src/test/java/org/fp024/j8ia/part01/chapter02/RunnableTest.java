package org.fp024.j8ia.part01.chapter02;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 2.4.2 Runnable로 코드 블록 실행하기
 * 
 * Logger가 실행됨을 테스트 할 수 있었던 것 같아서 아래 라이브러리르 활용해봤는데...
 * http://projects.lidalia.org.uk/slf4j-test/
 * 
 * * 일단은 스래드로 돌리는 내부 run 메서드의 로깅 캡쳐는 안되는 것 같다.
 * * Slf4j가 1개의 구현 로깅 라이브러리만 허용하여서, slf4j-test만 쓰게해야하는데..
 *   eclipse의 기본 JUnit 실행은 mvn test를 통해 실행하지않고, java를 직접 커맨드라인으로 실행해서
 *   surefire 의 라이브러리 제외설정이 안먹음. ㅠㅠ
 *   
 *   여기까지만 해봤고, 다른 괜찮은 라이브러리나 방법이 있을 경우 적용해본다.
 *   커밋은 해두고 이후부터는 제거해두자.. ==> 제거 했음 코드 확인시 이전 커밋 볼 것.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RunnableTest {
	RunnableBody runnableBody = new RunnableBody();

	@Order(1)
	@Test
	void testLegacy() throws InterruptedException {
		runnableBody.legacyRun();
	}

	@Order(2)
	@Test
	void testLamda() throws InterruptedException {
		runnableBody.lamdaRun();
	}

}
