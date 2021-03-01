package org.fp024.j8ia.part02.chapter05;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Transaction {
	private final Trader trader;
	private final int year;
	private final int value;
}
