package com.jnulocker.events.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record PagedLockersResponse(
        @Schema(description = "사물함 목록") List<LockerResponse> lockers,
        @Schema(description = "현재 페이지 번호", example = "0") int currentPage,
        @Schema(description = "전체 페이지 수", example = "5") int totalPages,
        @Schema(description = "전체 사물함 개수", example = "100") int totalElements,
        @Schema(description = "층 정보", example = "3층") String floorInfo) {

    public static PagedLockersResponse of(
            List<LockerResponse> lockers,
            int currentPage,
            int totalPages,
            int totalElements,
            String floorInfo) {
        return new PagedLockersResponse(lockers, currentPage, totalPages, totalElements, floorInfo);
    }
}
