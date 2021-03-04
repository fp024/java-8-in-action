package org.fp024.j8ia.part02.chapter06;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 통화
 * 
 * 맵의 키로 쓰일 수 있어서, Equals & HashCode를 구현하자.
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Currency {
	private final String name;
	private final String symbol;
}
