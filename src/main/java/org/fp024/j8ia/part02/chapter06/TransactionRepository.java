package org.fp024.j8ia.part02.chapter06;

import java.util.Arrays;
import java.util.List;

public class TransactionRepository {
	private TransactionRepository() {
		// 유틸리티 클래스 객체 생성방지
	}
	
	public static List<Transaction> getTransaction() {
		return Arrays.asList(
			  Transaction.builder().currency(CurrencyRepository.getCurrency("doller")).year(2020).value(1200).trader("Lee").build()
			, Transaction.builder().currency(CurrencyRepository.getCurrency("doller")).year(2019).value(1500).trader("Lee").build()
			, Transaction.builder().currency(CurrencyRepository.getCurrency("won")).year(2019).value(1550).trader("Ahn").build()
			, Transaction.builder().currency(CurrencyRepository.getCurrency("won")).year(2020).value(3000).trader("Ahn").build()
			, Transaction.builder().currency(CurrencyRepository.getCurrency("yen")).year(2018).value(1000).trader("Park").build()
			, Transaction.builder().currency(CurrencyRepository.getCurrency("yen")).year(2021).value(1100).trader("Park").build()
			, Transaction.builder().currency(CurrencyRepository.getCurrency("pound")).year(2017).value(500).trader("Choi").build()
			, Transaction.builder().currency(CurrencyRepository.getCurrency("pound")).year(2021).value(2300).trader("Choi").build()
		);	
	}
}
