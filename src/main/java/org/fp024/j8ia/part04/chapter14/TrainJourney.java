package org.fp024.j8ia.part04.chapter14;

import lombok.Getter;
import lombok.Setter;

@Getter
class TrainJourney {
	private final int price;
	private final String station;

	@Setter
	private TrainJourney onward;

	public TrainJourney(int price, String station, TrainJourney t) {
		this.price = price;
		this.station = station;
		this.onward = t;
	}
	
	public TrainJourney getLastOnward() {
		TrainJourney t = this;
		while (t.getOnward() != null) {
			t = t.getOnward();
		}
		return t;
	}
	
}
