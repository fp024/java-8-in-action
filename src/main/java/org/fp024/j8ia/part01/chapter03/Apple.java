package org.fp024.j8ia.part01.chapter03;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Apple implements Fruit {
	public Apple(Integer weight) {
		this.weight = weight;
	}

	public Apple(String color, Integer weight) {
		this.color = color;
		this.weight = weight;
	}

	private String color;
	private Integer weight;
	private String country;
}
