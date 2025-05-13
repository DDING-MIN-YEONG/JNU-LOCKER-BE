package com.jnulocker.common.persistence.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/** QueryDSL 정렬 유틸리티 클래스 Pageable 객체의 정렬 정보를 QueryDSL OrderSpecifier로 변환합니다. */
@UtilityClass
public class QueryDslOrderUtils {

    /**
     * PathProvider를 사용하여 Pageable의 정렬 정보를 기반으로 QueryDSL OrderSpecifier 배열을 생성합니다.
     *
     * @param pageable 정렬 정보를 포함한 Pageable 객체
     * @param pathProvider Path 매핑을 제공하는 PathProvider
     * @return OrderSpecifier 배열
     */
    public static OrderSpecifier<?>[] getOrderSpecifiers(
            Pageable pageable, PathProvider pathProvider) {
        return getOrderSpecifiers(
                pageable, pathProvider.getPathMap(), pathProvider.getDefaultPath(), Order.ASC);
    }

    /**
     * Pageable의 정렬 정보를 기반으로 QueryDSL OrderSpecifier 배열을 생성합니다.
     *
     * @param pageable 정렬 정보를 포함한 Pageable 객체
     * @param pathMap 속성 이름과 Path 객체의 매핑
     * @param defaultPath 기본 정렬에 사용할 대상 Path
     * @param defaultDirection 기본 정렬 방향 (기본값은 ASC)
     * @return OrderSpecifier 배열
     */
    private static OrderSpecifier<?>[] getOrderSpecifiers(
            Pageable pageable,
            Map<String, Path<?>> pathMap,
            Path<?> defaultPath,
            Order defaultDirection) {

        // 기본 정렬 방향이 지정되지 않은 경우 ASC로 설정
        if (defaultDirection == null) {
            defaultDirection = Order.ASC;
        }

        // 정렬이 지정되지 않은 경우 기본 정렬 반환
        if (pageable == null || !pageable.getSort().isSorted()) {
            return new OrderSpecifier[] {new OrderSpecifier(defaultDirection, defaultPath)};
        }

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            // Map에서 Path 찾기
            Path<?> path = pathMap.get(property);
            if (path != null) {
                orderSpecifiers.add(new OrderSpecifier(direction, path));
            }
        }

        // 정렬 지정자가 없는 경우 기본 정렬 반환
        return orderSpecifiers.isEmpty()
                ? new OrderSpecifier[] {new OrderSpecifier(defaultDirection, defaultPath)}
                : orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}
