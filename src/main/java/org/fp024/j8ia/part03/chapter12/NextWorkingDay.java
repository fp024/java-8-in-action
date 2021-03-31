package org.fp024.j8ia.part03.chapter12;

import java.time.DayOfWeek;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

/**
 * 퀴즈 12-2
 * 내가 한게 저자님 해결방식하고 좀 다른데..
 * 저자님 내용은 별도 클래스로 만들지 말고, 테스트 코드 내에 람다식으로 넣어두자... 
 */
class NextWorkingDay implements TemporalAdjuster {

	@Override
	public Temporal adjustInto(Temporal temporal) {

		// 다음 일의 temporal 구함.
		Temporal nextTemporal = temporal.plus(1, ChronoUnit.DAYS);

		// 다음일이 일요일이면 1일을 더함.
		if (nextTemporal.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SUNDAY.getValue()) {
			return nextTemporal.plus(1, ChronoUnit.DAYS);
		
		// 다음일이 토요일이면 2일을 더함.
		} else if (nextTemporal.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SATURDAY.getValue()) {
			return nextTemporal.plus(2, ChronoUnit.DAYS);
		}

		return nextTemporal;
	}

}
