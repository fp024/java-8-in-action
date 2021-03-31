package org.fp024.j8ia.part03.chapter12;

import static java.time.temporal.TemporalAdjusters.dayOfWeekInMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfNextMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfNextYear;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.firstInMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previous;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * 12.2 날짜 조정, 파싱, 포맷팅
 */
class ManipulatingParsingAndFormattingDatesTest {

	@Test
	void testAbsoluteWay() {
		LocalDate date1 = LocalDate.of(2021, 3, 25);
		LocalDate date2 = date1.withYear(2022);
		LocalDate date3 = date2.withDayOfMonth(1);
		LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 5);

		assertEquals(LocalDate.of(2022, 5, 1), date4);
	}

	@Test
	void testRelativeWay() {
		// LocalDate는 Temporal을 구현함.
		LocalDate date1 = LocalDate.of(2014, 3, 18);
		LocalDate date2 = date1.plusWeeks(1);

		assertEquals(LocalDate.of(2014, 3, 25), date2);

		LocalDate date3 = date2.minusYears(3);
		assertEquals(LocalDate.of(2011, 3, 25), date3);

		LocalDate date4 = date3.plus(6, ChronoUnit.MONTHS);
		assertEquals(LocalDate.of(2011, 9, 25), date4);
	}

	/**
	 * 퀴즈 12-1 LocalDate 조정
	 */
	@Test
	void testQuizLocalDateAdjust() {
		LocalDate date = LocalDate.of(2014, 3, 18);
		date = date.with(ChronoField.MONTH_OF_YEAR, 9); // 2014-09-18
		date = date.plusYears(2).minusDays(10); // 2016-09-08
		assertEquals(LocalDate.of(2011, 9, 8), date.withYear(2011));

		// date 자체를 바꾸지 않으므로, date 변수의 현재 값은 아래와 같다.
		assertEquals(LocalDate.of(2016, 9, 8), date);
	}

	/**
	 * 12.2.1 TemporalAdjusters 사용하기
	 * 
	 * 다음 주 일요일, 돌아오는 평일, 어떤달의 마지막날등 더 복잡한 날짜조정이 필요할때, with 메서드에 TemporalAdjuster를
	 * 전달하는 방법을 사용할 수 있음.
	 */
	@Test
	void testTemporalAdjusters() {
		LocalDate date1 = LocalDate.of(2014, 3, 18);
		LocalDate date2 = date1.with(nextOrSame(DayOfWeek.SUNDAY));

		// 현재 주 또는 같은 주의 파라미터로 넘어온 요일에 대한 날짜 반환.
		assertEquals(LocalDate.of(2014, 3, 23), date2);

		LocalDate date3 = date2.with(lastDayOfMonth());
		assertEquals(LocalDate.of(2014, 3, 31), date3);

	}

	@Test
	void testTemporalAdjustersFactoryMethod() {
		// dayOfWeekInMonth: 3월 두번째 주의 월요일
		assertEquals(LocalDate.of(2021, 3, 8), LocalDate.of(2021, 3, 10).with(dayOfWeekInMonth(2, DayOfWeek.MONDAY)));

		// firstDayOfMonth: 2021년 3월의 첫째 일자
		assertEquals(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 3, 10).with(firstDayOfMonth()));

		// firstDayOfNextMonth: 2021년 3월의 다음달의 첫째 일자
		assertEquals(LocalDate.of(2021, 4, 1), LocalDate.of(2021, 3, 10).with(firstDayOfNextMonth()));

		// firstDayOfNextYear: 2021년 3월의 다음년도 첫번째 일자 (3월 1일이 아니고, 1월 1일이다)
		assertEquals(LocalDate.of(2022, 1, 1), LocalDate.of(2021, 3, 10).with(firstDayOfNextYear()));

		// firstDayOfYear: 2021년 3월의 현재 년도 첫번째 일자
		assertEquals(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 3, 10).with(firstDayOfYear()));

		// firstInMonth: 2021년 3월의 첫번째 수요일의 일자
		assertEquals(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 3, 10).with(firstInMonth(DayOfWeek.WEDNESDAY)));

		// 2020년 2월이 윤년인데... 윤년기준으로 확인해보자.
		// lastDayOfMonth: 2020년 2월의 마지막 일자
		assertEquals(LocalDate.of(2020, 2, 29), LocalDate.of(2020, 2, 1).with(lastDayOfMonth()));

		// lastDayOfNextMonth: 다음 달의 마지막 일자
		// 그런데... AdoptOpenJdk에는 없고, Oracle JDK 8 명세에도 없음.
		// Oracle JDK 8 API 명세
		// https://docs.oracle.com/javase/8/docs/api/java/time/temporal/TemporalAdjusters.html
		// assertEquals(LocalDate.of(2020, 2, 29), LocalDate.of(2020, 1,
		// 1).with(lastDayOfNextMonth());
		assertEquals(LocalDate.of(2020, 2, 29), LocalDate.of(2020, 1, 1).plusMonths(1).with(lastDayOfMonth()));

		// lastDayOfYear: 2020년의 마지막 일자
		assertEquals(LocalDate.of(2020, 12, 31), LocalDate.of(2020, 1, 1).with(lastDayOfYear()));

		// next
		// 2021년 3월 10일이 수요일인데.. 다음번 수요일에 해당하는 일자 반환 (현재날짜 미포함.)
		assertEquals(LocalDate.of(2021, 3, 17), LocalDate.of(2021, 3, 10).with(next(DayOfWeek.WEDNESDAY)));

		// previous
		// 2021년 3월 10일이 수요일인데... 이전 수요일에 해당하는 일자 반환 (현재날짜 미포함.)
		assertEquals(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 3, 10).with(previous(DayOfWeek.WEDNESDAY)));

		// nextOrSame
		// 2021년 3월 10일이 수요일인데.. 다음번 수요일에 해당하는 일자 반환 (현재날짜 포함.)
		assertEquals(LocalDate.of(2021, 3, 10), LocalDate.of(2021, 3, 10).with(nextOrSame(DayOfWeek.WEDNESDAY)));

		// previousOrSame
		// 2021년 3월 10일이 수요일인데... 이전 수요일에 해당하는 일자 반환 (현재날짜 포함.)
		assertEquals(LocalDate.of(2021, 3, 10), LocalDate.of(2021, 3, 10).with(previousOrSame(DayOfWeek.WEDNESDAY)));

	}

	/**
	 * 퀴즈 12-2 커스텀 TemporalAdjuster 구현하기
	 * 
	 * TemporalAdjuster 인터페이스를 구현하는 NextWorkingDay 클래스를 구현하시오. 이 클래스는 날짜를 하루 씩 다음날로
	 * 바꾸는데, 이때 토요일과 일요일은 건너 뛴다. 즉, 다음 코드를 실행하면 다음 날로 이동한다.
	 * 
	 * date = date.with(new NextWorkingDay());
	 * 
	 * 만일 이동된 날짜가 평일이 아니라면, 즉 토요일이나 일요일이이라면, 월요일로 이동한다.
	 */
	@Test
	void testQuizImplementCustomTemporalAdjuster() {
		verifyQuiz(new NextWorkingDay());
	}

	/**
	 * 저자님 해결 방식
	 */
	@Test
	void testQuizImplementCustomTemporalAdjusterByWriter() {
		verifyQuiz(temporal -> {
			DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY) {
				dayToAdd = 3;
			} else if (dow == DayOfWeek.SATURDAY) {
				dayToAdd = 2;
			}
			return temporal.plus(dayToAdd, ChronoUnit.DAYS);
		});
	}

	/**
	 * TemporalAdjusters.ofDateAdjuster의 사용
	 * 
	 * TemporalAdjuster를 람다 표현식으로 정의하고 싶다면... ofDateAdjuster 를 사용하는 것이 좋음
	 */
	@Test
	void testTemporalAdjustersOfDateAdjuster() {
		TemporalAdjuster nextWorkingDay = TemporalAdjusters.ofDateAdjuster(temporal -> {
			DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY) {
				dayToAdd = 3;
			} else if (dow == DayOfWeek.SATURDAY) {
				dayToAdd = 2;
			}
			return temporal.plus(dayToAdd, ChronoUnit.DAYS);
		});
		verifyQuiz(nextWorkingDay);
	}

	void verifyQuiz(TemporalAdjuster t) {
		// 2021-03-25 목요일
		LocalDate date = LocalDate.of(2021, 3, 25);
		date = date.with(t);

		assertEquals(LocalDate.of(2021, 3, 26), date);

		// 2021-03-26 금요일
		date = LocalDate.of(2021, 3, 26);
		date = date.with(t);

		assertEquals(LocalDate.of(2021, 3, 29), date);

		// 2021-03-27 토요일
		date = LocalDate.of(2021, 3, 27);
		date = date.with(t);

		assertEquals(LocalDate.of(2021, 3, 29), date);
	}

	/**
	 * 12.2.2 날짜와 시간 객체 출력과 파싱
	 */
	@Test
	void testPrintingAndParsingDateTimeObjects() {
		LocalDate date = LocalDate.of(2021, 3, 31);
		assertEquals("20210331", date.format(DateTimeFormatter.BASIC_ISO_DATE));
		assertEquals("2021-03-31", date.format(DateTimeFormatter.ISO_LOCAL_DATE));

		assertEquals(date, LocalDate.parse("20210331", DateTimeFormatter.BASIC_ISO_DATE));
		assertEquals(date, LocalDate.parse("2021-03-31", DateTimeFormatter.ISO_LOCAL_DATE));

		// 패턴으로 DateTimeFormatter 만들기
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		assertEquals("31/03/2021", date.format(formatter));

		assertEquals(date, LocalDate.parse("31/03/2021", formatter));

		
		// 지역화된 DateTimeFormatter 만들기
		DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
		assertEquals("31. marzo 2021", date.format(italianFormatter));
		assertEquals(date, LocalDate.parse("31. marzo 2021", italianFormatter));
	}
	
	
	@Test
	void testCreateDateTimeFomatter() {
		DateTimeFormatter italianFormatter = new DateTimeFormatterBuilder()
				.appendText(ChronoField.DAY_OF_MONTH)
				.appendLiteral(". ")
				.appendText(ChronoField.MONTH_OF_YEAR)
				.appendLiteral(" ")
				.appendText(ChronoField.YEAR)
				.parseCaseInsensitive()
				.toFormatter(Locale.ITALIAN);
		LocalDate date = LocalDate.of(2021, 3, 31);
		assertEquals("31. marzo 2021", date.format(italianFormatter));
	}
}
