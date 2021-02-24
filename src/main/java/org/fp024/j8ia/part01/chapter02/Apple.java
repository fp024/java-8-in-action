package org.fp024.j8ia.part01.chapter02;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Apple {
	private Integer weight;
	private String color;
}
