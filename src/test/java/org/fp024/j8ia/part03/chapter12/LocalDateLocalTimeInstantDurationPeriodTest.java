package org.fp024.j8ia.part03.chapter12;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 12. 새로운 날짜와 시간 API
 * 
 * 12.1 LocalDate, LocalTime, Instant, Duration, Period
 */
@Slf4j
class LocalDateLocalTimeInstantDurationPeriodTest {
	@BeforeEach
	void before() {
		// JodaTime 의 경우 테스트를 위해 전역적으로 시간을 고정할 수 있는 유틸리티가 있었는데,
		// 비슷한 것이 있는지 확인을 해보자.
	}

	/**
	 * 12.1.1 LocalDate와 LocalTime 사용 펙터리 메서드 now()는 시스템 시계의 정보를 이용해 현재 날짜정보를 얻음.
	 */
	@Test
	void testWorkingWithLocalDateAndLocalTime() {
		LocalDate date = LocalDate.of(2021, 3, 29);
		assertEquals(2021, date.getYear(), "년도");
		assertEquals(Month.MARCH, date.getMonth(), "월");
		assertEquals(29, date.getDayOfMonth(), "일자");
		assertEquals(DayOfWeek.MONDAY, date.getDayOfWeek(), "요일");
		assertEquals(31, date.lengthOfMonth(), "3월의 일 수");
		assertFalse(date.isLeapYear(), "윤년이 아님.");

		// TemporalField를 이용하여 LocalDate 값 얻기
		assertEquals(2021, date.get(ChronoField.YEAR));
		assertEquals(3, date.get(ChronoField.MONTH_OF_YEAR));
		assertEquals(29, date.get(ChronoField.DAY_OF_MONTH));

		// LocalTime 만들고 값 읽기
		LocalTime time = LocalTime.of(13, 30, 50);
		assertEquals(13, time.getHour());
		assertEquals(30, time.getMinute());
		assertEquals(50, time.getSecond());

		// parse 메서드 사용
		assertEquals(LocalDate.of(2021, 3, 29), LocalDate.parse("2021-03-29"));
		assertEquals(LocalTime.of(13, 30, 50), LocalTime.parse("13:30:50"));

	}

	/**
	 * 12.1.2 날짜와 시간 조합
	 */
	@Test
	void testCombiningADateAndATime() {
		LocalTime time = LocalTime.of(12, 23, 33);
		LocalDate date = LocalDate.of(2021, Month.MARCH, 30);

		LocalDateTime dt1 = LocalDateTime.of(2021, Month.MARCH, 30, 12, 23, 33);
		LocalDateTime dt2 = LocalDateTime.of(date, time);
		assertEquals(dt2, dt1);

		LocalDateTime dt3 = LocalDate.of(2021, Month.MARCH, 30).atTime(12, 23, 33);
		assertEquals(dt3, dt1);

		LocalDateTime dt4 = date.atTime(time);
		assertEquals(dt4, dt1);

		LocalDateTime dt5 = time.atDate(date);
		assertEquals(dt5, dt1);

		// LocalDateTime에서 LocalDate, LocalTime 추출하기.
		LocalDate date1 = dt1.toLocalDate();
		assertEquals(date, date1);

		LocalTime time1 = dt2.toLocalTime();
		assertEquals(time, time1);
	}

	/**
	 * 12.1.3 Instant: 기계의 날짜 시간
	 * 
	 * 사람은 주, 날짜, 시, 분, 초로 날짜와 시간을 계산하지만 기계의 관점에서는 연속된 시간에서 특정지점을 하나의 큰수로 표현하는 것이
	 * 자연스럽다.
	 * 
	 * Instant: Unix Epoch Time(1970년 1월 1일 0시 0분 0초 UTC)를 기준으로 특정 지점까지의 시간을 초로 표현.
	 */
	@Test
	void testInstantADateAndTimeForMachines() {
		Instant instant1 = Instant.ofEpochSecond(3);
		// 1970-01-01T00:00:03Z
		logger.info(instant1.toString());

		Instant instant2 = Instant.ofEpochSecond(3, 0);
		assertEquals(instant2, instant1);

		Instant instant3 = Instant.ofEpochSecond(2, 1_000_000_000); // 2초 이후, 이후의 1억 나노초(1초)
		assertEquals(instant3, instant1);

		Instant instant4 = Instant.ofEpochSecond(4, -1_000_000_000); // 2초 이후, 이후의 1억 나노초(1초)
		assertEquals(instant4, instant1);

		Instant instant5 = Instant.ofEpochSecond(2, -10_000_000_000L); // 2초 이후, 이전의 10억 나노초(10초)
		// 1969-12-31T23:59:52Z
		logger.info(instant5.toString());

		// Instant는 년, 월, 일 등의 정보는 제공하지 않는다.
		// NANO_OF_SECOND, MILLI_OF_SECOND, MILLI_OF_SECOND, INSTANT_SECONDS 는 제공한다.
		Instant now = Instant.now();
		logger.info(now.toString()); // 2021-03-30T03:50:58.404Z
		assertThrows(UnsupportedTemporalTypeException.class, () -> now.get(ChronoField.DAY_OF_YEAR));
	}

	/**
	 * 12.1.4 Duration과 Period 의 정의
	 * 
	 * Duration: 두 시간 객체 사이의 지속시간
	 * 
	 * Period: 년, 월, 일로 시간을 표현할 때 사용
	 * 
	 */
	@Test
	void testDefiningADurationOrAPeriod() {
		Duration d1 = Duration.between(LocalTime.of(1, 1, 1), LocalTime.of(2, 1, 10));
		assertEquals(3609, d1.getSeconds());

		Duration d2 = Duration.between(LocalDateTime.of(2021, Month.MARCH, 30, 1, 1, 1),
				LocalDateTime.of(2021, Month.MARCH, 31, 2, 1, 10));
		assertEquals(60 * 60 * 24 + 3609, d2.getSeconds(), "시간만 뽑아서 계산하는게 아니라 일단위 까지 초로 차이를 낼 수 있다 (당연해야하지만..)");

		Instant now = Instant.now();
		Instant before10Seconds = now.minusSeconds(10);
		Duration d3 = Duration.between(before10Seconds, now);
		assertEquals(10, d3.getSeconds());

		Period days = Period.between(LocalDate.of(2020, Month.MARCH, 21), LocalDate.of(2021, Month.MARCH, 31));
		assertEquals(10, days.getDays(), "년도의 간격은 별도로 비교함 365 * 1 + 10 이 아님.");
		assertEquals(1, days.getYears(), "년도의 간격은 별도로 비교함");
		
		
		Duration threeMinutes = Duration.ofMinutes(3);
		assertEquals(3 * 60, threeMinutes.getSeconds());		
		assertEquals(threeMinutes, Duration.of(3, ChronoUnit.MINUTES));
		
		
		Period tenDays = Period.ofDays(10);
		assertEquals(10, tenDays.getDays());
		
		Period threeWeeks = Period.ofWeeks(3);
		assertEquals(3 * 7, threeWeeks.getDays());
		
		// 2년 6개월 1일
		Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
		assertEquals(1, twoYearsSixMonthsOneDay.getDays());
		assertEquals(1, twoYearsSixMonthsOneDay.get(ChronoUnit.DAYS));
		assertEquals(6, twoYearsSixMonthsOneDay.getMonths());
		assertEquals(2, twoYearsSixMonthsOneDay.getYears());

	}

}