package org.fp024.j8ia.part04.chapter14;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
class Tree {
    private String key;
    private int value;
    private Tree left;
    private Tree right;

    // 오브젝트가 그대로 사용되었는지 확인 목적
    private final String objectId = super.toString().replace(getClass().getCanonicalName() + "@", "");

    public Tree(String key, int value, Tree left, Tree right) {
        this.key = key;
        this.value = value;
        this.left = left;
        this.right = right;
    }
}

class TreeProcessor {
    // 검색
    public static int lookup(String key, int defaultValue, Tree t) {
        if (t == null) {
            return defaultValue;
        }
        if (key.equals(t.getKey())) {
            return t.getValue();
        }
        return lookup(key, defaultValue, key.compareTo(t.getKey()) < 0 ? t.getLeft() : t.getRight());
    }

    // 업데이트
    public static Tree update(String key, int newValue, Tree t) {
        if (t == null) {
            t = new Tree(key, newValue, null, null);
        } else if (key.equals(t.getKey())) {
            t.setValue(newValue);
        } else if (key.compareTo(t.getKey()) < 0) {
            t.setLeft(update(key, newValue, t.getLeft()));
        } else {
            t.setRight(update(key, newValue, t.getRight()));
        }
        return t;
    }

    // 함수형 업데이트
    // 새로운 트리를 만들긴하는데...
    //
    public static Tree funcUpdate(String key, int newValue, Tree t) {
        return (t == null) ?
                new Tree(key, newValue, null, null) :
                key.equals(t.getKey()) ?
                        new Tree(key, newValue, t.getLeft(), t.getRight()) :
                        key.compareTo(t.getKey()) < 0 ?
                                new Tree(t.getKey(), t.getValue(), funcUpdate(key, newValue, t.getLeft()), t.getRight()) :
                                new Tree(t.getKey(), t.getValue(), t.getLeft(), funcUpdate(key, newValue, t.getRight()));
    }

}
