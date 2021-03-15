package org.fp024.j8ia.part03.chapter08;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
class Point {
	static final Comparator<Point> compareByXAndThenY = 
			Comparator.comparing(Point::getX)
				.thenComparing(Point::getY);
	
	private final int x;
	private final int y;

	Point moveRightBy(int x) {
		return new Point(this.x + x, y);
	}
	
	static List<Point> moveAllPointsRightBy(List<Point> points, int x) {
		return points.stream().map(p -> new Point(p.getX() + x, p.getY()))
				.collect(Collectors.toList());
	}	
}
