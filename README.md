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
    
* 265쪽 코드
    * `logger.isLoggable(Log.FINER) ==> logger.isLoggable(Level.FINER)`
	* 레벨 지정 부분이 잘못된 것 같습니다.
    	* JRE의 logging.properties에 ConsoleHandler의 기본 로그레벨이 INFO이기 때문에, 해당 로그레벨을 FINER 이하로 낮춰야 메시지가 노출될거라는 것에 대해서도 간단히 언급되면 좋을 것 같습니다.
    
* 280쪽 문구
    
* 따로 테스트가 실패했다는 말이없고, 빨리 넘어간 느낌이라, Object의 기본 구현 때문에 List<Point>의 동등비교가 실패함을 약간 부연설명을 더해주는게 좋을 것 같습니다.
    
* 304쪽 퀴즈
    * A 인터페이스의 hello() default메서드를 D클래스에서 오버라이드하는데, 오버라이드한 메서드의 접근 지정자가 public이 되어야할 것 같습니다.
        * `void hello () { ==> public void hello () {`
        * 인터페이스의 모든 메서드는 public 접근 수준을 가지고 있는데, 그부분을 빼고 오버라이드 하였기 때문에 컴파일 오류가 발생합니다.
            `Cannot reduce the visibility of the inherited method from A`
        * 306쪽의 C클래스 내부의 hello() 도 접근지정자가 public 이어야 합니다.

* 323쪽 flatMap 활용 부분, 예제 10-5
    flatMap의 함수형 전달 코드 블록의 리턴값은 NonNull을 요구 하기 때문에, (Adpot OpenJDK 1.8 285 코드 봤을 때..)
    예제 10-4 에 정의된 Person, Car를 그대로 사용할 수 없습니다.
    해당 도메인들은 Optional<Car> car, Optional<Insurance> insurance가 null이 될 수 있기 때문에,
    null 인 상태로 flatMap을 사용시 함수형 전달 코드 블럭의 getCar()가
    반환하는 값이 null이 되어 NPE오류가 발생합니다.

    * Optional map의 반환 부분 JDK구현
        `return Optional.ofNullable(mapper.apply(value));`
    * Optional flatMap의 반환 부분 JDK구현
        `return Objects.requireNonNull(mapper.apply(value));`

    * 기존 도메인 코드
    ```java
    class Person {
        private Optional<Car> car;

        public Optional<Car> getCar() {
            return car;
        }
    }

    class Car {
        private Optional<Insurance> insurance;

        public Optional<Insurance> getInsurance() {
            return insurance;
        }
    }

    // ....
    ```

    * 수정 도메인 코드 (Optional 결과가 절대 null을 반환하지 않도록 수정)
    ```java
    class Person {
        private Car car;

        public Optional<Car> getCar() {
            return Optional.ofNullable(car);
        }
    }

    static class Car {
        private Insurance insurance;

        public Optional<Insurance> getInsurance() {
            return Optional.ofNullable(insurance);
        }
    }
    // ....
    ```
    
* 379쪽 표 12-3  TemporalAdjusters 의 정적 팩토리 메서드 중, `lastDayOfNextMonth` 란 메서드는 없음.

    * 집필시점에는 해당 메서드가 있었을지는 모르겠는데, Oracle JDK8, AdoptOpenJDK8 모두 해당 메서드가 없습니다.
    * Oracle JDK 8 TemporalAdjusters API 명세
        * https://docs.oracle.com/javase/8/docs/api/java/time/temporal/TemporalAdjusters.html

* 385쪽 ZoneId를 이용해서 LocalDateTime을 Instant로 바꾸는 방법에서 LocalDateTime의 toInstant 사용관련

  ```java
  LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
  Instant instantFromDateTime = dateTime.toInstant(romeZone);
  // romeZone의 완전한 코드는 없으나 앞쪽의 내용으로 봐서는 ZoneId 타입인데..
  // LocalDateTime의 toInstant 메서드는 ZoneOffset 타입을 전달 인자로 받음.
  ```

  

### 정오표
* 230쪽 그림 7-1
    * 스트림에 들어있는 값이 모두 3으로 나타납니다. 1, 2, 3, 4 그리고 5, 6, 7, 8 이 되어야합니다.
* 299쪽 
    * 파라미터로 넘어온 변수명이 잘못기입되어있습니다.
        * `getRotationAngle() + angle ==> getRotationAngle() + angleInDegrees`
* 377 쪽 예제 12-7
    * date2에 대한 예상 값이 잘못되었습니다.
        * 2014-03-18 ===>  2014-03-25

* 383쪽 예제 12-11
  * 4번째 줄에 date.format() 이 아니라 date1.format()이 되야할 것 같습니다.

* 386쪽
  * 4개의 클래가 -> 4개의 클래스가