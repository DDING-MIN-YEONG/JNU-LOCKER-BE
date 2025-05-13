package com.jnulocker.common.persistence.querydsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.StringPath;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class QueryDslOrderUtilsTest {

    private PathProvider pathProvider;
    private Path<?> defaultPath;
    private Map<String, Path<?>> pathMap;

    @BeforeEach
    void setUp() {
        pathMap = new HashMap<>();

        StringPath titlePath = mock(StringPath.class);
        StringPath createdAtPath = mock(StringPath.class);

        pathMap.put("title", titlePath);
        pathMap.put("createdAt", createdAtPath);

        pathProvider = mock(PathProvider.class);
        when(pathProvider.getPathMap()).thenReturn(pathMap);

        doReturn(defaultPath).when(pathProvider).getDefaultPath();
    }

    @Test
    @DisplayName("정렬이 없는 Pageable로 OrderSpecifier 생성 시 기본 정렬이 적용되어야 한다")
    void getOrderSpecifiersWithUnsortedPageable() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        OrderSpecifier<?>[] orderSpecifiers =
                QueryDslOrderUtils.getOrderSpecifiers(pageable, pathProvider);

        // then
        assertNotNull(orderSpecifiers);
        assertEquals(1, orderSpecifiers.length);
        assertEquals(Order.ASC, orderSpecifiers[0].getOrder());
        assertEquals(defaultPath, orderSpecifiers[0].getTarget());
    }

    @Test
    @DisplayName("정렬이 있는 Pageable로 OrderSpecifier 생성 시 해당 정렬이 적용되어야 한다")
    void getOrderSpecifiersWithSortedPageable() {
        // given
        Sort sort = Sort.by(Sort.Direction.DESC, "title");
        Pageable pageable = PageRequest.of(0, 10, sort);

        // when
        OrderSpecifier<?>[] orderSpecifiers =
                QueryDslOrderUtils.getOrderSpecifiers(pageable, pathProvider);

        // then
        assertNotNull(orderSpecifiers);
        assertEquals(1, orderSpecifiers.length);
        assertEquals(Order.DESC, orderSpecifiers[0].getOrder());
        assertEquals(pathMap.get("title"), orderSpecifiers[0].getTarget());
    }

    @Test
    @DisplayName("여러 정렬 조건이 있는 Pageable로 OrderSpecifier 생성 시 모든 정렬이 적용되어야 한다")
    void getOrderSpecifiersWithMultipleSortCriteria() {
        // given
        Sort sort = Sort.by(Sort.Order.desc("title"), Sort.Order.asc("createdAt"));
        Pageable pageable = PageRequest.of(0, 10, sort);

        // when
        OrderSpecifier<?>[] orderSpecifiers =
                QueryDslOrderUtils.getOrderSpecifiers(pageable, pathProvider);

        // then
        assertNotNull(orderSpecifiers);
        assertEquals(2, orderSpecifiers.length);

        assertEquals(Order.DESC, orderSpecifiers[0].getOrder());
        assertEquals(pathMap.get("title"), orderSpecifiers[0].getTarget());

        assertEquals(Order.ASC, orderSpecifiers[1].getOrder());
        assertEquals(pathMap.get("createdAt"), orderSpecifiers[1].getTarget());
    }

    @Test
    @DisplayName("매핑되지 않은 정렬 속성이 있는 경우 해당 속성은 무시되어야 한다")
    void getOrderSpecifiersWithUnmappedSortProperty() {
        // given
        Sort sort = Sort.by(Sort.Order.desc("title"), Sort.Order.asc("nonExistentProperty"));
        Pageable pageable = PageRequest.of(0, 10, sort);

        // when
        OrderSpecifier<?>[] orderSpecifiers =
                QueryDslOrderUtils.getOrderSpecifiers(pageable, pathProvider);

        // then
        assertNotNull(orderSpecifiers);
        assertEquals(1, orderSpecifiers.length);
        assertEquals(Order.DESC, orderSpecifiers[0].getOrder());
        assertEquals(pathMap.get("title"), orderSpecifiers[0].getTarget());
    }

    @Test
    @DisplayName("모든 정렬 속성이 매핑되지 않은 경우 기본 정렬이 적용되어야 한다")
    void getOrderSpecifiersWithAllUnmappedSortProperties() {
        // given
        Sort sort =
                Sort.by(
                        Sort.Order.desc("nonExistentProperty1"),
                        Sort.Order.asc("nonExistentProperty2"));
        Pageable pageable = PageRequest.of(0, 10, sort);

        // when
        OrderSpecifier<?>[] orderSpecifiers =
                QueryDslOrderUtils.getOrderSpecifiers(pageable, pathProvider);

        // then
        assertNotNull(orderSpecifiers);
        assertEquals(1, orderSpecifiers.length);
        assertEquals(Order.ASC, orderSpecifiers[0].getOrder());
        assertEquals(defaultPath, orderSpecifiers[0].getTarget());
    }
}
