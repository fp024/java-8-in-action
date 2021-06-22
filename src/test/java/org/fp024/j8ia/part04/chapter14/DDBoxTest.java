package org.fp024.j8ia.part04.chapter14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DDBoxTest {
	@Test
	void testToString() {
		DDBox<Double, Integer> d = new DDBox<>();
		d.set(1.1, 5);
		
		assertEquals("1.1\n1.1", d.toString());
		assertEquals(d.toString02(), d.toString(), "toString02()와 toString()의 결과는 같음");
	}

}

class DDBox<U, D> {
	private U up;
	private D down;

	public void set(U u, D d) {
		up = u;
		down = d;
	}

	public String toString() {
		return up.toString() + "\n" + up;
	}
	
	// 첫번째 up의 toString()을 생략해도 됨
	public String toString02() {
		return up + "\n" + up;
	}
}
