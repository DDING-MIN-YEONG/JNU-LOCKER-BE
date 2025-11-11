package com.jnulocker.events.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record LockerSummaryResponse(
        @Schema(description = "전체 사물함 개수", example = "150") int totalLockers,
        @Schema(description = "사용 가능한 사물함 개수", example = "45") int availableLockers,
        @Schema(description = "층별 사물함 집계 정보") List<FloorSummary> floors) {

    public static LockerSummaryResponse of(
            int totalLockers, int availableLockers, List<FloorSummary> floors) {
        return new LockerSummaryResponse(totalLockers, availableLockers, floors);
    }
}
