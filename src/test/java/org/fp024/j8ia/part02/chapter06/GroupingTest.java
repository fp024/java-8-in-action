package org.fp024.j8ia.part02.chapter06;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.fp024.j8ia.part02.chapter05.Dish;
import org.fp024.j8ia.part02.chapter05.Dish.Type;
import org.fp024.j8ia.part02.chapter05.DishRepository;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 6.3 그룹화
 */
@Slf4j
class GroupingTest {
	// 5장에서 사용한 데이터를 재사용한다.
	private final List<Dish> menu = DishRepository.getDishList();
	
	@Test
	void testGrouping() {
		Map<Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));		
		logger.info(dishesByType.toString());

		// 복잡한 분류가 요구되면 메서드 레퍼런스 구현할 수 없는 경우가 있음, 이때른 람다식으로 직접 구현할 수 있다.
		// 		Eclipse상에서, 문장이 어느정도 완성되기 전까지는 참조에 대한 자동완성이 잘 안된다. 
		Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(groupingBy(
				dish -> { 
					if (dish.getCalories() <= 400) {
						return CaloricLevel.DIET;
					} else if (dish.getCalories() <= 700) {
						return CaloricLevel.NORMAL;
					} else {
						return CaloricLevel.FAT;
					}
				}
			));
		logger.info(dishesByCaloricLevel.toString());
		
	}
	
	
	/**
	 * 6.3.1 다수준 그룹화
	 */
	@Test
	void testMultilevelGrouping() {
		// 음식의 타입으로 그룹화한후, 그룹화한 각 요소를 다시 칼로리 레벨로 그룹화
		Map<Type, Map<Object, List<Dish>>> dishesByTypeCaloricLevel = menu.stream().collect(
			groupingBy(Dish::getType, groupingBy(
				dish -> { 
					if (dish.getCalories() <= 400) {
						return CaloricLevel.DIET;
					} else if (dish.getCalories() <= 700) {
						return CaloricLevel.NORMAL;
					} else {
						return CaloricLevel.FAT;
					}
				})
		));
		
		// {  MEAT={DIET=[chicken], NORMAL=[beef], FAT=[pork]}, 
		//    FISH={NORMAL=[salmon], DIET=[prawns]}, 
		//    OTHER={DIET=[rice, season fruit], NORMAL=[french fries, pizza]} }
		logger.info(dishesByTypeCaloricLevel.toString());
	}
	
	
	/**
	 * 6.3.2 서브그룹으로 데이터 수집
	 */
	@Test
	void testCollectingDataInSubgroups() {
		Map<Type, Long> typesCount = menu.stream().collect(groupingBy(Dish::getType, counting()));
		
		// {OTHER=4, FISH=2, MEAT=3}
		logger.info(typesCount.toString());
		
		
		// 요리 타입 별 가장 높은 칼로리를 가진 요리
		Map<Type, Optional<Dish>> mostCaloricByType = menu.stream().collect(
				groupingBy(
					Dish::getType
				  , maxBy(Comparator.comparingInt(Dish::getCalories))
				 )
		);
		// {OTHER=Optional[pizza], FISH=Optional[salmon], MEAT=Optional[pork]}
		logger.info(mostCaloricByType.toString());
		
		
		
		// 컬렉터 결과를 다른 형식에 적용하기
		Map<Type, Dish> mostCaloricByTypeRemoveOptional = menu.stream()
				.collect(groupingBy(Dish::getType,
								collectingAndThen(
										maxBy(Comparator.comparingInt(Dish::getCalories))
									 , Optional::get
								)
							)
				);
		// {OTHER=pizza, FISH=salmon, MEAT=pork}
		logger.info(mostCaloricByTypeRemoveOptional.toString());

		
		
		
		// groupingBy와 함께 사용하는 다른 컬랙터 예제
		// 요리 타입별 요리 칼로리의 합계
		Map<Type, Integer> totalCaloricsByType = menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));

		// {OTHER=1550, FISH=750, MEAT=1900}
		logger.info(totalCaloricsByType.toString());
		
		
		
		// 각 요리 형식에 존재하는 모든 CaloricLevel 값을 조회 
		// mapping: 입력요소를 누적하기 전에 매핑 함수를 적용해서 다양한 형식의 객체를 주어진 형식의 컬렉터에 맞게 변환하는 역활을 함.
		Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream().collect(
			groupingBy(Dish::getType, mapping(dish -> { 
				if (dish.getCalories() <= 400) {
					return CaloricLevel.DIET;
				} else if (dish.getCalories() <= 700) {
					return CaloricLevel.NORMAL;
				} else {
					return CaloricLevel.FAT;
				}
			}, toSet())
		));
		// {OTHER=[DIET, NORMAL], FISH=[DIET, NORMAL], MEAT=[FAT, DIET, NORMAL]}
		logger.info(caloricLevelsByType.toString());
		
		
		  Map<Type, Set<CaloricLevel>> caloricLevelsByTypeUseToCollection = menu.stream().collect(
				groupingBy(Dish::getType, mapping(dish -> { 
						if (dish.getCalories() <= 400) {
							return CaloricLevel.DIET;
						} else if (dish.getCalories() <= 700) {
							return CaloricLevel.NORMAL;
						} else {
							return CaloricLevel.FAT;
						}
					},
					toCollection(HashSet::new)
					)
			));
		
		//{OTHER=[DIET, NORMAL], FISH=[DIET, NORMAL], MEAT=[FAT, DIET, NORMAL]}
		logger.info(caloricLevelsByTypeUseToCollection.toString());
	}

}
