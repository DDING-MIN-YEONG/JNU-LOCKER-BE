package com.jnulocker.events.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record AvailableLockersResponse(
        @Schema(description = "사용 가능한 사물함 목록 (층 정보 포함)") List<LockerWithFloorInfo> lockers,
        @Schema(description = "현재 페이지 번호", example = "0") int currentPage,
        @Schema(description = "전체 페이지 수", example = "3") int totalPages,
        @Schema(description = "전체 사용 가능한 사물함 개수", example = "45") int totalElements) {

    public static AvailableLockersResponse of(
            List<LockerWithFloorInfo> lockers, int currentPage, int totalPages, int totalElements) {
        return new AvailableLockersResponse(lockers, currentPage, totalPages, totalElements);
    }
}
