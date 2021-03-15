package org.fp024.j8ia.part03.chapter08;

import java.util.function.Consumer;

public class OnlineBankingLambda {
	/**
	 * makeCustomerHappy 메서드가 처리하던 부분을 Cunsumer로 받음
	 */
	public void processCustomer(int id, Consumer<Customer> makeCustomerHappy) {
		Customer c = DataBase.getCustomerWithId(id);
		makeCustomerHappy.accept(c);
	}
}
