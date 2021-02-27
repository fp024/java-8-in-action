package org.fp024.j8ia.part01.chapter03;

public class Letter {
	private Letter() {
		// 유틸리티 클래스 객체생성 방지
	}
	
	public static String addHeader(String text) {
		return "From Raoul, Mario and Alan: " + text;
	}
	
	public static String addFooter(String text) {
		return text + " Kind regards";
	}
	
	public static String checkSpelling(String text) {
		// target 에 정규 표현식이 아닌이상 replaceAll보다는 replace를 쓰는것이 성능상 좋다고함. 
		return text.replace("labda", "lambda");
	}
}
