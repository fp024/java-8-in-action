package org.fp024.j8ia.part01.chapter02;

public class AppleGreenColorPredicate implements ApplePredicate {
	@Override
	public boolean test(Apple apple) {
		return "green".equals(apple.getColor());
	}
}
