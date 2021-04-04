package org.fp024.j8ia.part04.chapter13;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 13.2.4 함수형 실전 연습
 *
 * List<Integer> = {1, 4, 9} 가 주어졌을 때.. 이것의 모든 서브 집합의 멤버로 구성된
 * List<List<Integer>>를 만드는 프로그램
 * 
 * {1, 4, 9} 의 서브 집합은 아래처럼 8개가 존재함.
 * 
 * {1, 4, 9}, {1, 4}, {1, 9}, {9, 4} {1}, {4}, {9}, {}
 */
@Slf4j
class FunctionalStyleInPracticeTest {
	@Test
	void test() {
		List<List<Integer>> result = subsets(Arrays.asList(1, 4, 9));
		logger.info("final result: {}", result);

		assertEquals(8, result.size());
	}

	List<List<Integer>> subsets(List<Integer> list) {
		logger.info("list: {}", list);
		
		// 입력 리스트가 비었다면 빈 리스트 자신이 서브집합!
		if (list.isEmpty()) {
			List<List<Integer>> ans = new ArrayList<>();
			ans.add(Collections.emptyList());
			return ans;
		}

		Integer first = list.get(0);
		List<Integer> rest = list.subList(1, list.size()); // list의 크기가 1이라면 빈배열을 반환한다.

		// 빈 리스트가 아니면 먼저 하나의 요소를 꺼내고 나머지 요소의
		// 모든 서브집합을 찾아서 subans 로전달한다.
		// subans는 절반의 정답을 포함한다.
		List<List<Integer>> subans = subsets(rest);
		
		// 정답의 나머지 절반을 포함하는 subans2는
		// subans의 모든 리스트에 처음 꺼낸 요소를앞에 추가해서 만든다.
		List<List<Integer>> subans2 = insertAll(first, subans);

		// subans, subans2를 연결하면 정답이 완성됨.. 저자님은 쉽다고 한다 ㅠㅠ
		return concat(subans, subans2);
		
	}
	
	// []
	// 
	
	
	
	
	

	List<List<Integer>> insertAll(Integer first, List<List<Integer>> lists) {
		logger.info("first: {}, lists: {}", first, lists);
		List<List<Integer>> result = new ArrayList<>();

		for (List<Integer> list : lists) {
			// 리스트를 복사한 다음에 리스트에 요소를 추가한다.
			// 구조차게 가변이라도 저수준 구조를 복사하진 않는다,
			// Integer는 가변이 아니다 <?>
			List<Integer> copyList = new ArrayList<>();
			copyList.add(first);
			copyList.addAll(list);
			result.add(copyList);
		}
		logger.info("result: {}", result);
		return result;
	}

	List<List<Integer>> concat(List<List<Integer>> a, List<List<Integer>> b) {
		List<List<Integer>> r = new ArrayList<>(a);
		r.addAll(b);
		return r;
	}

	
	@Test
	void testSubList() {
		List<Integer> list = Arrays.asList(9);
		List<Integer> rest = list.subList(1, list.size());
		assertTrue(rest.isEmpty());
	}
	
}
