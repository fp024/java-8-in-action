package org.fp024.j8ia.part02.chapter06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 6장. 스트림으로 데이터 수집
 * 
 * 전장에서 언급한 Transaction 과 비슷하게 사용은 하는데, Currency를 도메인으로 받았음.
 */
@Slf4j
class CollectionDataWithStreamsTest {

	private List<Transaction> transactions = TransactionRepository.getTransaction();
	
	/**
	 * 통화별로 트랜젝션
	 */
	@Test
	void testLegacyAndStream() {
		// 스트림을 사용하지 않은 그룹핑
		Map<Currency, List<Transaction>> legacyTransactionsByCurrencies = new HashMap<>();

		for(Transaction transaction : transactions) {
			Currency currency = transaction.getCurrency();
			
			List<Transaction> transactionsForCurrency = legacyTransactionsByCurrencies.get(currency);
			
			if (transactionsForCurrency == null) {
				transactionsForCurrency = new ArrayList<>();
				legacyTransactionsByCurrencies.put(currency, transactionsForCurrency);
			}
			
			transactionsForCurrency.add(transaction);
		}

		legacyTransactionsByCurrencies.forEach((t, u) -> {
			logger.info(t.toString());
			u.forEach(e -> logger.info("\t{}", e.toString()));
		});
		
		// 스트림을 사용한 그룹핑, Collectors.groupingBy()에 위의 동작들이 내제된 것 같다.
		Map<Currency, List<Transaction>> streamTransactionsByCurrencies  
			= transactions.stream().collect(Collectors.groupingBy(Transaction::getCurrency));

		assertEquals(legacyTransactionsByCurrencies, streamTransactionsByCurrencies);
	}

}
