package org.fp024.j8ia.part04.chapter14;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 14.1 함수는 모든 곳에 존재한다
 */
@Slf4j
class FunctionsEverywhereTest {
    @Test
    void testMethodReference() {
        Function<String, Integer> strToInt = Integer::parseInt;
        assertEquals(12345, strToInt.apply("12345"));
    }

    @Test
    void testHigherOrderFunctionsTest() {
        Function<Double, Double> a = (Double x) -> x * x;
        Function<Function<Double, Double>, Function<Double, Double>> c = (b) -> b.andThen(x -> 2 * x);
        assertEquals(18, c.apply(a).apply(3.0));
    }

    // 섭씨를 화시로 계산하는 함수
    // Function<Double, Double> cToF = (Double x) -> x * 9 / 5 + 32;

    /**
     * 변환 패턴으로 나타냄.
     *
     * @param x 변환하려는 값
     * @param f 변환 요소
     * @param b 기준치 조정 요소
     * @return 변환된 값
     */
    static double converter(double x, double f, double b) {
        return x * f + b;
    }

    /**
     * 커링이라는 개념을 활용해서 한개의 인수를 갖는 변환함수 생성 -> x와 y라는 인자를 받는 함수 f를 한 개의 인수를 받는 g라는 함수로
     * 대체하는 기법
     */
    static DoubleUnaryOperator curriedConverter(double f, double b) {
        return (double x) -> x * f + b;
    }

    @Test
    void testCurrying() {
        DoubleUnaryOperator convertCtoF = curriedConverter(9.0 / 5, 32);
        assertEquals(89.6, convertCtoF.applyAsDouble(32));

        // 달러 대비 파운드 환율은 저자님 시점과 다르긴하니..
        DoubleUnaryOperator convertUSDtoGBP = curriedConverter(0.6, 0);
        assertEquals(0.6, convertUSDtoGBP.applyAsDouble(1));

        // 킬로미터를 마일로...
        DoubleUnaryOperator convertKmtoMi = curriedConverter(0.6214, 0);
        assertEquals(0.6214 * 2, convertKmtoMi.applyAsDouble(2));
    }

    static TrainJourney link(TrainJourney a, TrainJourney b) {
        if (a == null) {
            return b;
        }
        TrainJourney t = a;
        while (t.getOnward() != null) {
            t = t.getOnward();
        }
        t.setOnward(b);
        return a;
    }

    @Test
    void testSideEffectLink() {
        TrainJourney seoul = new TrainJourney(1000, "서울역", null);

        TrainJourney kumi = new TrainJourney(2000, "구미역", null);

        TrainJourney busan = new TrainJourney(3000, "부산역", null);

        // 서울역에서 구미역 까지의 기차 여행
        TrainJourney sToK = link(seoul, kumi);
        assertEquals("구미역", sToK.getLastOnward().getStation());

        // 서울역에서 부산까지의 기차 여행
        TrainJourney sToB = link(seoul, busan);
        assertEquals("부산역", sToB.getLastOnward().getStation());

        // 저자님이 말하고자 하는 부분이...
        // 위와 같은 식으로 link를 기존정보를 바꾸게 되는 상태가 될 때..
        // 서울에서 구미까지의 연결이 뒤의 부산역 연결 link 작업에 의해 바뀌는 상태가 되는 문제를 지적했다.
        assertEquals("부산역", sToK.getLastOnward().getStation());
    }

    /**
     * 부작용이 없는 Link
     */
    static TrainJourney append(TrainJourney a, TrainJourney b) {
        if (a == null) {
            return b;
        } else {
            return new TrainJourney(a.getPrice(), a.getStation(), append(a.getOnward(), b));
        }
    }

    /**
     * 부작용이 없는 Append
     */
    @Test
    void testNoSideEffectAppend() {
        TrainJourney seoul = new TrainJourney(1000, "서울역", null);

        TrainJourney kumi = new TrainJourney(2000, "구미역", null);

        TrainJourney busan = new TrainJourney(3000, "부산역", null);

        // 서울역에서 구미역 까지의 기차 여행
        TrainJourney sToK = append(seoul, kumi);
        assertEquals("구미역", sToK.getLastOnward().getStation());

        // 서울역에서 부산까지의 기차 여행
        TrainJourney sToB = append(seoul, busan);
        assertEquals("부산역", sToB.getLastOnward().getStation());

        // 연결시점의 상태객체를 새로 만들어서 반환했기 때문에, 새로운 변경이 이전 변경에 영향을 미치지 않는다.
        assertEquals("구미역", sToK.getLastOnward().getStation());
    }


    /**
     * 저자님의 의도는...
     * 기존 참조를 통해 새로운 트리가 추가되어도 이전 시점의 변경을 피하고 싶다는 의도 같은데...
     * <p>
     * 3
     * \
     * 4
     * <p>
     * 3
     * / \
     * 2   4
     */
    @Test
    void testTreeUpdate() {
        Tree tree = TreeProcessor.update("3", 3, null);

        assertNull(tree.getLeft());
        assertNull(tree.getRight());
        assertEquals(3, tree.getValue());

        logger.info("tree :{}", tree);
        Tree treeUpdated1 = TreeProcessor.update("4", 4, tree);

        assertNull(treeUpdated1.getLeft());
        assertNotNull(treeUpdated1.getRight());
        assertEquals(3, treeUpdated1.getValue());
        assertEquals(4, treeUpdated1.getRight().getValue());
        logger.info("tree :{}", tree);
        logger.info("treeUpdated1 :{}", treeUpdated1);

        Tree treeUpdated2 = TreeProcessor.update("2", 2, treeUpdated1);

        assertNotNull(treeUpdated2.getLeft());
        assertNotNull(treeUpdated1.getRight());
        assertEquals(3, treeUpdated1.getValue());
        assertEquals(2, treeUpdated1.getLeft().getValue());
        assertEquals(4, treeUpdated1.getRight().getValue());

        logger.info("tree :{}", tree);
        logger.info("treeUpdated1 :{}", treeUpdated1);
        logger.info("treeUpdated2 :{}", treeUpdated2);
    }


    /**
     * 업데이트 될 때마다 새로운 트리로 반환
     * <p>
     * 트리의 모든 요소가 새로 만들어지진 않는다.
     * 트리에서 변화가 일어난 요소만 새로만들어진다.
     */
    @Test
    void testTreeFuncUpdate() {
        Tree tree = TreeProcessor.funcUpdate("3", 3, null);

        assertNull(tree.getLeft());
        assertNull(tree.getRight());
        assertEquals(3, tree.getValue());

        logger.info("tree: {}", tree);
        Tree treeUpdated1 = TreeProcessor.funcUpdate("4", 4, tree);

        // 최초 시작점의 트리요소는 새로 만든다.
        assertNotEquals(tree.getObjectId(), treeUpdated1.getObjectId());

        // "4" 업데이트 이후 기존 자료구조 변경이 없음.
        assertNull(tree.getLeft());
        assertNull(tree.getRight());
        assertEquals(3, tree.getValue());

        // "4" 가 업데이트 된 새로운 트리
        assertNull(treeUpdated1.getLeft());
        assertNotNull(treeUpdated1.getRight());
        assertEquals(3, treeUpdated1.getValue());
        assertEquals(4, treeUpdated1.getRight().getValue());


        logger.info("tree: {}", tree);
        logger.info("treeUpdated1 :{}", treeUpdated1);

        Tree treeUpdated2 = TreeProcessor.funcUpdate("2", 2, treeUpdated1);

        // 최초 시작점의 트리는 새로 만들어짐 (419쪽 14-4 그림과 비슷한 유형)
        assertNotEquals(treeUpdated1.getObjectId(), treeUpdated2.getObjectId());
        // 2 요소를 입력했을 때 기존의 오른쪽의 4요소는 새로운 트리에도 그대로 사용된다.
        assertEquals(treeUpdated1.getRight().getObjectId(), treeUpdated2.getRight().getObjectId());

        // "2" 업데이트 이후 기존 자료구조 변경이 없음.
        assertNull(tree.getLeft());
        assertNull(tree.getRight());
        assertEquals(3, tree.getValue());

        // "4" 가 업데이트 된 새로운 트리에도 변경없음.
        assertNull(treeUpdated1.getLeft());

        // "2"가 업데이트 된 새로운 트리
        assertNotNull(treeUpdated2.getLeft());
        assertNotNull(treeUpdated2.getRight());
        assertEquals(3, treeUpdated2.getValue());
        assertEquals(4, treeUpdated2.getRight().getValue());
        assertEquals(2, treeUpdated2.getLeft().getValue());

        logger.info("tree: {}", tree);
        logger.info("treeUpdated1 :{}", treeUpdated1);
        logger.info("treeUpdated2 :{}", treeUpdated2);
    }

    /**
     * 책에서는 lookup에 대한 설명은 따로 없긴 했는데, 기능확인만 해보자.
     * 데이터는 p419의 그림 14-4 로 입력... 6개의 요소를 넣어보자.
     */
    @Test
    void testLookUp() {
        // 그런데 정렬순으로 넣지 않더라도 결과는 정렬이 되어있어야한다.
        Tree tree01 = TreeProcessor.funcUpdate("Mary", 22, null);
        /*
            Tree(key=Mary, value=22,
                left=null,
                right=null,
              objectId=52de51b6)
        */
        logger.info("{}", tree01);


        Tree tree02 = TreeProcessor.funcUpdate("Emily", 20, tree01);
        /*
            Tree(key=Mary, value=22,
                left=Tree(key=Emily, value=20,
                            left=null,
                            right=null,
                       objectId=32c8e539),
                right=null,
             objectId=73dce0e6)
        */
        logger.info("{}", tree02);

        Tree tree03 = TreeProcessor.funcUpdate("Alan", 50, tree02);
        /*
            Tree(key=Mary, value=22,
                left=Tree(key=Emily, value=20,
                            left=Tree(key=Alan, value=50,
                                       left=null,
                                       right=null,
                                     objectId=5a85c92),
                             right=null,
                          objectId=32811494),
                right=null,
              objectId=4795ded0)
         */
        logger.info("{}", tree03);


        Tree tree04 = TreeProcessor.funcUpdate("Tian", 29, tree03);
        /*
             Tree(key=Mary, value=22,
                    left=Tree(key=Emily, value=20,
                            left=Tree(key=Alan, value=50,
                                        left=null,
                                        right=null,
                                    objectId=5a85c92),
                            right=null,
                          objectId=32811494),
                    right=Tree(key=Tian, value=29,
                                left=null,
                                right=null,
                            objectId=53dacd14),
                objectId=14d14731)
        */

        logger.info("{}", tree04);

        Tree tree05 = TreeProcessor.funcUpdate("Georgie", 23, tree04);

        /*
            Tree(key=Mary, value=22,
                left=Tree(key=Emily, value=20,
                        left=Tree(key=Alan, value=50,
                                left=null,
                                right=null,
                               objectId=5a85c92),
                        right=Tree(key=Georgie, value=23,
                                   left=null,
                                   right=null,
                                 objectId=2eced48b),
                      objectId=47c4ecdc),
                right=Tree(key=Tian, value=29,
                         left=null,
                         right=null,
                       objectId=53dacd14),
             objectId=42f33b5d)
        */
        logger.info("{}", tree05);

        Tree tree06 = TreeProcessor.funcUpdate("Raoul", 23, tree05);
        /*
            Tree(key=Mary, value=22,
                left=Tree(key=Emily, value=20,
                          left=Tree(key=Alan, value=50,
                                    left=null,
                                    right=null,
                                  objectId=5a85c92),
                          right=Tree(key=Georgie, value=23,
                                    left=null,
                                    right=null,
                                  objectId=2eced48b),
                        objectId=47c4ecdc),
                right=Tree(key=Tian, value=29,
                            left=Tree(key=Raoul, value=23,
                                        left=null,
                                        right=null,
                                    objectId=5c8504fd),
                            right=null,
                         objectId=4b7e96a),
             objectId=6475472c)
        */
        logger.info("{}", tree06);


        Tree tree07 = TreeProcessor.funcUpdate("Will", 26, tree06);
        /*
            Tree(key=Mary, value=22,
                left=Tree(key=Emily, value=20,
                         left=Tree(key=Alan, value=50,
                                    left=null,
                                    right=null,
                                  objectId=5a85c92),
                          right=Tree(key=Georgie, value=23,
                                    left=null,
                                    right=null,
                                  objectId=2eced48b),
                        objectId=47c4ecdc),
                right=Tree(key=Tian, value=29,
                          left=Tree(key=Raoul, value=23,
                                    left=null,
                                    right=null,
                                  objectId=5c8504fd),
                          right=Tree(key=Will, value=26,
                                   left=null,
                                   right=null,
                                 objectId=3f07b12c),
                        objectId=4bd1f8dd),
            objectId=7096b474)
         */
        logger.info("{}", tree07);


        assertEquals(29, TreeProcessor.lookup("Tian", -1, tree07));

        assertEquals(23, TreeProcessor.lookup("Georgie", -1, tree07));

        assertEquals(26, TreeProcessor.lookup("Will", -1, tree07));

        assertEquals(-1, TreeProcessor.lookup("Will", -1, tree06));
    }

}
