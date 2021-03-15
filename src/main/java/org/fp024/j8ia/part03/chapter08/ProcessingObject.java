package org.fp024.j8ia.part03.chapter08;

import lombok.Setter;

abstract class ProcessingObject<T> {
	@Setter
	protected ProcessingObject<T> successor;

	T handle(T input) {
		T r = handleWork(input);
		if (successor != null) {
			return successor.handle(r);
		}
		return r;
	}

	protected abstract T handleWork(T input);
}

class HeaderTextProcessing extends ProcessingObject<String> {
	@Override
	protected String handleWork(String input) {
		return "From Raoul, Mario and Alan: " + input;
	}
}

class SpellCheckerProcessing extends ProcessingObject<String> {
	@Override
	protected String handleWork(String input) {
		return input.replace("labda", "lambda");
	}
}
