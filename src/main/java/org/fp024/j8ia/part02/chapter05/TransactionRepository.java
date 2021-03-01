package org.fp024.j8ia.part02.chapter05;

import java.util.Arrays;
import java.util.List;

public class TransactionRepository {
	private  TransactionRepository() {
		// 유틸리티 클래스 객체 생성 방지
	}

	public static List<Transaction> getTransaction() {
		return Arrays.asList(
			  new Transaction(TraderRepository.getTrader("brian"), 2011, 300)
			, new Transaction(TraderRepository.getTrader("raoul"), 2012, 1000)
			, new Transaction(TraderRepository.getTrader("raoul"), 2011, 400)
			, new Transaction(TraderRepository.getTrader("mario"), 2012, 710)
			, new Transaction(TraderRepository.getTrader("mario"), 2012, 700)
			, new Transaction(TraderRepository.getTrader("alan"), 2012, 950)
		);
	}
}
