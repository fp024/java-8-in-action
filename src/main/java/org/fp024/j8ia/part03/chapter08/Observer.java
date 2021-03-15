package org.fp024.j8ia.part03.chapter08;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 패키지가 같아 테스트 클래스에서 접근이 가능하여.. 챕터에 지역적인 클래스는 일부러 public 안해도 될 것 같다. 예제를 한눈에 보도록
 * 한 파일에 여러 클래스 정의하는게 나을 수 있겠다.
 */
interface Observer {
	void notify(String tweet);
}

@Slf4j
class NYTimes implements Observer {
	@Override
	public void notify(String tweet) {
		if (tweet != null && tweet.contains("money")) {
			logger.info("Breaking news in NY! {}", tweet);
		}
	}
}

@Slf4j
class Guardian implements Observer {
	@Override
	public void notify(String tweet) {
		if (tweet != null && tweet.contains("queen")) {
			logger.info("Yet another news in London... {}", tweet);
		}
	}
}

@Slf4j
class LeMonde implements Observer {
	@Override
	public void notify(String tweet) {
		if (tweet != null && tweet.contains("wine")) {
			logger.info("Today cheese, wine and news! {}", tweet);
		}
	}
}

/**
 * 주제
 */
interface Subject {
	void registerObserver(Observer o);

	void notifyObervers(String tweet);
}

class Feed implements Subject {
	private final List<Observer> observers = new ArrayList<>();

	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void notifyObervers(String tweet) {
		observers.forEach(o -> o.notify(tweet));
	}
}
