package org.fp024.j8ia.part02.chapter06;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Collector<T, A, R>
 * 	T: 스트림 요소의 형식
 *  A: 누적자 형식 
 *  R: 수집 연산 결과의 형식
 */
public class PrimeNumbersCollector implements Collector<Integer,  Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

	// DBI:Double Brace Initialization(이중 중괄호 초기화)
	// SonarLint에서는 명시하는 것이 낫다고 한다.  Minor 수준 경고이긴함.
	// - 소유 개체의 인스턴스에 대한 참조가있는 익명 클래스를 만들기 때문에 익명 내부 클래스가 다른 개체에 의해 반환되고 유지되는 경우
	//   이 클래스를 사용하면 메모리 누수가 발생할 수 있음.
	// - 유지 보수 담당자가 혼동할 수 있음.
	/*
	@Override
	public Supplier<Map<Boolean, List<Integer>>> supplier() {
		return () -> new HashMap<Boolean, List<Integer>>() {{  
			put(true, new ArrayList<Integer>());
			put(false, new ArrayList<Integer>());
		}};
	}
	*/
	
	@Override
	public Supplier<Map<Boolean, List<Integer>>> supplier() {
		return () -> {
			Map<Boolean, List<Integer>> initMap = new HashMap<>();   // 두 개의 빈 리스트를 포함하는 맵으로 수집 동작 지시.
			initMap.put(true, new ArrayList<>());
			initMap.put(false, new ArrayList<>());
			return initMap;
		};
	}
	

	@Override
	public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
		return (Map<Boolean, List<Integer>> acc, Integer candidate) -> 
			acc.get(isPrime(acc.get(true), candidate)) // isPrime 결과에 따라 소수 리스트와 비소수 리스트를 만든다.
				.add(candidate); // isPrime메서드 결과에 따라 맵에서 알맞은 리스트를 받아 현재 candidate를 추가한다.
	}

	// 알고리즘 자체가 순차적이여서 컬렉터를 실제 병렬로 사용할 수는 없음.
	// 학습목적으로 구현했고, 빈구현 또는 UnsupportedOperationException 를 던지도록 할 수 있음.
	@Override
	public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
		return (Map<Boolean, List<Integer>> map1, 
				Map<Boolean, List<Integer>> map2) -> {
					map1.get(true).addAll(map2.get(true)); // 두번째 맵을 첫번째 맵에 병합
					map1.get(false).addAll(map2.get(false));
					return map1;
				};
	}

	@Override
	public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
		return Function.identity(); // 누적된 내용하고 컬랙터 결과가 같으므로 누적 결과를 결과로서 바로 반환 (항등)
	}

	@Override
	public Set<Characteristics> characteristics() {
		// 발견한 소수의 순서에 의미가 있으므로 콜랙터는 UNORDERED, CONCURRENT는 아님.
		return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
	}

	
	
	public static boolean isPrime(List<Integer> primes, int candidate) {
		int candidateRoot = (int) Math.sqrt((double) candidate);
		return takeWhile(primes, i -> i<= candidateRoot )
				.stream()
				.noneMatch(i -> candidate % i == 0);
	}
	
	
	public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
		int i = 0;
		for(A item : list) {
			if (!p.test(item)) { // 리스트의 현재 요소가 Predicate를 만족하는지 검사
				return list.subList(0, i); // Predicate를 만족하지 않으면 검사한 항목 앞쪽에 위치한 서브 리스트를 반환
			}
			i++;
		}
		
		return list; // 리스트의 모든항목이 Predicate를 만족하므로 리스트 자체를 반환.
	}
}




