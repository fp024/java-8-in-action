package org.fp024.j8ia.part02.chapter05;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TraderRepository {
	private TraderRepository() {
		// 유틸리티 클래스 객체 생성 방지
	}

	private static final Map<String, Trader> TRADER_MAP;

	static {
		Map<String, Trader> traderMap = new HashMap<>();
		traderMap.put("raoul", new Trader("Raoul", "Cambridge"));
		traderMap.put("mario", new Trader("Mario", "Milan"));
		traderMap.put("alan", new Trader("Alan", "Cambridge"));
		traderMap.put("brian", new Trader("Brian", "Cambridge"));
		TRADER_MAP = Collections.unmodifiableMap(traderMap);
	}

	public static Trader getTrader(String name) {
		return TRADER_MAP.get(name.toLowerCase());
	}

}
