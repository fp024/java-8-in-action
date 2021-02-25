package org.fp024.j8ia.part01.chapter02;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RunnableBody {
	public void legacyRun() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				logger.info("Hello World Legacy");
			}
		}).start();
	}

	public void lamdaRun() {
		Thread t = new Thread(() -> logger.info("Hello World Java 8 Lamda"));
		t.start();
	}
}
