package org.fp024.j8ia.part02.chapter06;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CurrencyRepository {
	private CurrencyRepository() {
		// 유틸리티 클래스 객체 생성 방지
	}

	private static final Map<String, Currency> CURRENCY_MAP;

	static {
		Map<String, Currency> currencyMap = new HashMap<>();
		currencyMap.put("doller", Currency.builder().name("doller").symbol("$").build());
		currencyMap.put("won", Currency.builder().name("won").symbol("₩").build());
		currencyMap.put("yen", Currency.builder().name("yen").symbol("¥").build());
		currencyMap.put("pound", Currency.builder().name("pound").symbol("£").build());

		CURRENCY_MAP = Collections.unmodifiableMap(currencyMap);
	}

	public static Currency getCurrency(String name) {
		return CURRENCY_MAP.get(name.toLowerCase());
	}

}
