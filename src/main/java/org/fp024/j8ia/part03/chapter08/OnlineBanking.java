package org.fp024.j8ia.part03.chapter08;

/**
 * SonarLint가 구현이 있는 메서드는 default 메서드로 바꾸고, 클래스 자체를 인터페이스로 사용하라고 한다.
 * (마이너 수준 경고)
 */
public abstract class OnlineBanking {
	public void processCustomer(int id) {
		Customer c = DataBase.getCustomerWithId(id);
		makeCustomerHappy(c);
	}
	
	abstract void makeCustomerHappy(Customer c);
}
