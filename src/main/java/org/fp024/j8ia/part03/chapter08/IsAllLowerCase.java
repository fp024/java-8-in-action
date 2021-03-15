package org.fp024.j8ia.part03.chapter08;

public class IsAllLowerCase implements ValidationStrategy {
	@Override
	public boolean execute(String s) {
		return s.matches("[a-z]+");
	}
}
