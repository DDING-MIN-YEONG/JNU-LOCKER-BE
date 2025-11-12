package com.jnulocker.events.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FloorSummary(
        @Schema(description = "층 번호", example = "3") int floorNumber,
        @Schema(description = "해당 층의 전체 사물함 개수", example = "50") int totalLockers,
        @Schema(description = "해당 층의 사용 가능한 사물함 개수", example = "15") int availableLockers) {

    public static FloorSummary of(int floorNumber, int totalLockers, int availableLockers) {
        return new FloorSummary(floorNumber, totalLockers, availableLockers);
    }
}
