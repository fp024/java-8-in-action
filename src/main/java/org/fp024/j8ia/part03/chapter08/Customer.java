package org.fp024.j8ia.part03.chapter08;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor // final 대상 맴버변수만 생성자 인자로 만들어줌.
public class Customer {
	private final int id;
	private final String name;
}
