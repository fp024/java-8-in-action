package org.fp024.j8ia.part03.chapter08;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBase {
	private static final Map<Integer, Customer> customerMap;

	static {
		Map<Integer, Customer> map = new HashMap<>();
		IntStream.rangeClosed(1, 5).forEach(i -> map.put(i, new Customer(i, "고객" + i)));
		customerMap = Collections.unmodifiableMap(map);
	}

	public static Customer getCustomerWithId(int id) {
		return customerMap.get(id);
	}
}
