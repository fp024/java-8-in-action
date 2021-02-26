package org.fp024.j8ia.part01.chapter03;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Apple implements Fruit {
	public Apple(Integer weight) {
		this.weight = weight;
	}

	private String color;
	private Integer weight;
}
