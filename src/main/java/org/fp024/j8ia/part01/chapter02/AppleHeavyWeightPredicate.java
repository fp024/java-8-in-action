package org.fp024.j8ia.part01.chapter02;

public class AppleHeavyWeightPredicate implements ApplePredicate {
	@Override
	public boolean test(Apple apple) {
		return apple.getWeight() >= 150;
	}
}
