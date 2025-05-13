package com.jnulocker.common.persistence.querydsl;

import com.querydsl.core.types.Path;
import java.util.Map;

/** QueryDSL Path 매핑을 제공하는 인터페이스 엔티티별 정렬 필드와 경로를 매핑하기 위해 사용됩니다. */
public interface PathProvider {

    /**
     * 필드명과 Path 객체의 매핑을 반환합니다.
     *
     * @return 필드명과 Path 객체의 Map
     */
    Map<String, Path<?>> getPathMap();

    /**
     * 기본 정렬에 사용될 Path를 반환합니다.
     *
     * @return 기본 정렬 Path
     */
    Path<?> getDefaultPath();
}
