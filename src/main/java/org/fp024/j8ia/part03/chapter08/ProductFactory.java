package org.fp024.j8ia.part03.chapter08;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ProductFactory {
	public static Product createProduct(String name) {
		switch (name) {
		case "loan":
			return new Loan();
		case "stock":
			return new Stock();
		case "bond":
			return new Bond();
		default:
			throw new IllegalArgumentException("No such product " + name);
		}
	}
}

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ProductFactoryLambdas {
	static final Map<String, Supplier<Product>> map = new HashMap<>();

	static {
		map.put("loan", Loan::new);
		map.put("stock", Stock::new);
		map.put("bond", Bond::new);
	}

	public static Product createProduct(String name) {
		Supplier<Product> p = map.get(name);

		if (p == null) {
			throw new IllegalArgumentException("No such product " + name);
		} else {
			return p.get();
		}
	}
}

interface Product {
	String getName();
}

class Loan implements Product {
	@Override
	public String getName() {
		return "대출";
	}
}

class Stock implements Product {
	@Override
	public String getName() {
		return "주식";
	}
}

class Bond implements Product {
	@Override
	public String getName() {
		return "채권";
	}
}