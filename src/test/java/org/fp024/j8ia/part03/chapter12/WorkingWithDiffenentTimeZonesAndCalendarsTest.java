package org.fp024.j8ia.part03.chapter12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 12.3 다양한 시간대와 캘린더 활용방법
 */
@Slf4j
class WorkingWithDiffenentTimeZonesAndCalendarsTest {
	@Test
	void testZoneId() {
		logger.info("\n{}", ZoneId.getAvailableZoneIds().toString().replace(", ", "\n\t"));
		
		// 윈도우 로케일에 맞춰진 것 같다.
		ZoneId zoneId = TimeZone.getDefault().toZoneId();
		assertEquals("Asia/Seoul", zoneId.getId());
	}
	
	/**
	 * 특정 시점 시간대 적용
	 */
	@Test
	void testApplyingATimeZoneToAPointInTime() {
		LocalDate date = LocalDate.of(2021, Month.MARCH, 31);
		
		
		ZoneId romeZone = ZoneId.of("Europe/Rome");
		ZonedDateTime zd1 = date.atStartOfDay(romeZone);
		// zd1: 2021-03-31T00:00+02:00[Europe/Rome]
		logger.info("zd1: {}", zd1.toString());
		
		LocalDateTime dateTime = LocalDateTime.of(2021, Month.MARCH, 31, 8, 30, 50);
		ZonedDateTime zd2 = dateTime.atZone(romeZone);
		// zd2: 2021-03-31T08:30:50+02:00[Europe/Rome]
		logger.info("zd2: {}", zd2.toString());
		
		
		Instant instant = Instant.now();
		ZonedDateTime zd3 = instant.atZone(romeZone);
		// 2021-03-31T01:33:37.930+02:00[Europe/Rome]
		logger.info("zd3: {}", zd3.toString());
	}
	
	/**
	 * 
	 * https://stackoverflow.com/questions/52944410/convert-localdatetime-to-instant-requires-a-zoneoffset-why
	 */
	@Test
	void testLocalDateTimeToInstantOrInstantLocalDateTime() {
		ZoneId romeZone = ZoneId.of("Europe/Rome");
		
		LocalDateTime dateTime = LocalDateTime.of(2021, Month.MARCH, 18, 13, 45);
		ZoneOffset zoneOffset = romeZone.getRules().getOffset(dateTime);
		
		// Instant instantFormDateTime = dateTime.toInstant(romeZone);
		Instant instantFormDateTime = dateTime.toInstant(zoneOffset);
		// 저자님은 toInstant에 ZoneId 타입이 들어갈 수 있다는 설명을 했는데,
		// ZoneOffset 타입을 전달해줘야했다. 
		
		// 2021-03-18T12:45:00Z
		logger.info("{}", instantFormDateTime);
		
		LocalDateTime timeFromInstant = LocalDateTime.ofInstant(instantFormDateTime, romeZone);
		
		// 2021-03-18T13:45
		logger.info("{}", timeFromInstant);
		
		assertEquals(dateTime, timeFromInstant);
		
	}
	
	
	/**
	 * 12.3.1 UTC/GMT 기준의 고정 오프셋
	 */
	@Test
	void testFixedOffsetFromUTCGreenwich() {
		/*
			한국시간 GMT +9 
			뉴욕시간 GMB -5
			
			뉴욕시간은 한국 시간에서 14시간 전...
		*/
		ZoneOffset newYorkOffset = ZoneOffset.of("-05:00");
		
		LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
		
		OffsetDateTime dateTimeInNewYork = OffsetDateTime.of(dateTime, newYorkOffset);
		
		// 2014-03-18T13:45-05:00
		logger.info("{}", dateTimeInNewYork);
		
	}
	
	
	/**
	 * 12.3.2 대안 캘린더 시스템 사용하기
	 * 	 ThaiBuddhistDate
	 *   MinguoDate
	 *   JapaneseDate
	 *   HijraDate
	 * 
	 * 
	 * Java 8에서는 추가로 위의 4개 캘린더 시스템을 제공함.
	 * 그러나 날짜와 시간 API의 설계자는 프로그램의 입출력을 지역화하는 상황을 제외하고는
	 * LocalDate를 사용을 권장함.
	 */
	@Test
	void testUsingAlternativeCalendarSystems() {
		LocalDate date = LocalDate.of(2021, 3, 31);
		JapaneseDate japaneseDate = JapaneseDate.from(date);
		
		// Japanese Reiwa 3-03-31
		logger.info("{}", japaneseDate);
		
		Chronology japaneseChronology = Chronology.ofLocale(Locale.JAPAN);
		
		logger.info("{}", japaneseChronology.dateNow());
		
	}
	
	
	@Test
	void testIslamicCalendar() {
		// 현재 Hijrah 날짜를 얻음, 얻은 날짜를 Ramadan의 첫번째 날, 즉 9번째 달로 바꿈.
		HijrahDate ramadanDate = HijrahDate.now().with(ChronoField.DAY_OF_MONTH, 1)
						.with(ChronoField.MONTH_OF_YEAR, 9);
		
		
		logger.info("\n\tRamadan starts on {} and ends on {}"
				  // IsoChronology.INSTANCE 는 IsoChronology 클래스의 정적 인스턴스임.
				, IsoChronology.INSTANCE.date(ramadanDate)
				
  				  // 2021년 Ramadan은 2021-04-13 일에 시작해서  2021-05-12  일에 종료함.
				, IsoChronology.INSTANCE.date(ramadanDate.with(TemporalAdjusters.lastDayOfMonth()))
		);
	}
	
	/**
	 * 12.4 요약
	 * 	- 새로운 날짜와 시간 API에서 날짜와 시간객체는 모두 불변이다.
	 *  - TemporalAdjuster를 이용하면 단순히 값을 바꾸는 것 이상의 복잡한 동작을 수행할 수 있고,
	 *    자신만의 커스텀 날짜 변환 기능을 정의할 수 있다.
	 * 
	 * 
	 * 
	 */
}
