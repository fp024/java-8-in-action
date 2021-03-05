package org.fp024.j8ia.part02.chapter06;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 트랜젝션
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Transaction {
	private final Currency currency;
	private final String trader;
	private final int year;
	private final int value;
}
