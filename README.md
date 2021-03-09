## Java 8 in Action 스터디
* 저자: Raoul-Gabriel Urma, Mario Fusco, Alan Mycroft
* yes24 도서 판매 링크 (2021년 현시점 품절 상태)
	* www.yes24.com/Product/Goods/17252419

* 내용
	* Java 8 in Action 책의 내용을 연습합니다.<br>
	코드의 실행은 Junit5 로 동작시키고, 출력은 Log4j2로 출력합니다.<br>
	Part 및 Chapter 별로 Java 패키지를 나눕니다.<br>
	프로젝트 분리를 할 필요는 없을 것 같은데, 필요시 분리하도록 합니다.
    
### 의견
* 퀴즈 정답을 제공해주는 것은 아주 좋은데,<br>정답을 부록에 몰아서 보여주면 좋을 것 같습니다.<br>바로 아래 답이 있어서, 자꾸 풀기도 전에 답이보이네요 ㅠㅠ

* 6.2.3 문자열 연결 항목(Joining Strings)의 예제 코드 중 특정 코드가 AdoptJDK 1.8.0_282 에서 테스트시 컴파일 오류가 납니다. String으로 넘겨줘야 정상동작합니다.
    * 컴파일 오류
	```
	The method collect(Collector<? super Dish,A,R>) in the type Stream<Dish> is not applicable 
	for the arguments (Collector<CharSequence,capture#15-of ?,String>)
	```
    * 대상 코드
    	* `menu.stream().collect(joining());`
