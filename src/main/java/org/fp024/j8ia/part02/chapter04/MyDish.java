package org.fp024.j8ia.part02.chapter04;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MyDish {
	private String name;
	private int calories;
}
