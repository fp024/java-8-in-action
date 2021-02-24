package org.fp024.j8ia.part01.chapter02;

public class AppleRedAndHeavyPredicate implements ApplePredicate {
	@Override
	public boolean test(Apple apple) {
		return "red".equals(apple.getColor()) && apple.getWeight() >= 150;
	}
}
