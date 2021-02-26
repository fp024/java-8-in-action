package org.fp024.j8ia.part01.chapter03;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Orange implements Fruit {
	private Integer weight;

	public Orange(Integer weight) {
		this.weight = weight;
	}
	
	@Override
	public String getColor() {
		return "orange";
	}
}
