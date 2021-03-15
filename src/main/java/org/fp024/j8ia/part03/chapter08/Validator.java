package org.fp024.j8ia.part03.chapter08;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Validator {
	private final ValidationStrategy strategy;

	public boolean validate(String s) {
		return strategy.execute(s);
	}
}
