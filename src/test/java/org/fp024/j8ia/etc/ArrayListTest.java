package org.fp024.j8ia.etc;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArrayListTest {
    @Test
    void testGrow() {
        List<String> list = new ArrayList<>(5);
        // ArrayList 내부의 Object 배열 참조 얻기
        final Object[] elementData01 = (Object[]) ReflectionTestUtils.getField(list, "elementData");
        
        // 초기 용량 값을 설정해준대로 5 크기의 Object 배열이 할당됨.
        assertArrayEquals(new Object[]{null, null, null, null, null}, elementData01);

        list.add("111");
        assertArrayEquals(new Object[]{"111", null, null, null, null}, elementData01);

        list.add("222");
        assertArrayEquals(new Object[]{"111", "222", null, null, null}, elementData01);

        list.add("333");
        assertArrayEquals(new Object[]{"111", "222", "333", null, null}, elementData01);

        list.add("444");
        assertArrayEquals(new Object[]{"111", "222", "333", "444", null}, elementData01);

        list.add("555");
        assertArrayEquals(new Object[]{"111", "222", "333", "444", "555"}, elementData01);


        // =============== 초기 용량 크기가 5 초과되면 ...
        list.add("666"); // 초과 되는 데이터 추가
        final Object[] elementData02 = (Object[]) ReflectionTestUtils.getField(list, "elementData");

        assertNotEquals(elementData02, elementData01, "내부의 Object[] 이 새로운 크기의 객체로 교체됨");

        assertArrayEquals(new Object[]{"111", "222", "333", "444", "555"}, elementData01, "elementData01는 ArrayList에서 참조가 끊김, 버려진 오브젝트 배열");

        // AdoptOpenJDK 1.8 에서는 추가한 데이터 사이즈 + 1 만큼 Object[] 배열을 만들고 있음.
        assertArrayEquals(new Object[]{"111", "222", "333", "444", "555", "666", null}, elementData02);

        list.add("777");
        assertArrayEquals(new Object[]{"111", "222", "333", "444", "555", "666", "777"}, elementData02);

    }


    @Test
    void testHashCode() {
        // 리스트의 hashCode는 리스트의 요소를 기준으로 계산하기 때문에,
        // initialCapacity 를 설정해준 것과는 관계가 없습니다.

        List<Integer> list = new ArrayList<>(5);
        int hashCode01 = list.hashCode();

        list.add(1);
        int hashCode02 = list.hashCode();
        assertNotEquals(hashCode02, hashCode01, "최초 아무것도 없을 때의 해시값과 값을 하나 추가했을 때의 해시 값이 다르다.");

        list.add(2);
        int hashCode03 = list.hashCode();

        assertNotEquals(hashCode03, hashCode02);

        list.remove(1);
        int hashCode04 = list.hashCode();

        assertEquals(hashCode04, hashCode02, "요소를 다시지워서 맞췄을 때, 해시코드 값이 같음을 확인할 수 있다.");
    }


    @Test
    void testUnmodifiableList() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        List<Integer> unmodifiableList = Collections.unmodifiableList(list);

        assertThrows(UnsupportedOperationException.class, () -> {
            unmodifiableList.add(4);
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            unmodifiableList.remove(0);
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            unmodifiableList.set(0, 9);
        });
    }
}
