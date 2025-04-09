package com.jnulocker.events.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record FloorWithLockersResponse(
        @Schema(description = "층 ID", example = "1") Long floorId,
        @Schema(description = "층 번호", example = "1") Integer floorNumber,
        @Schema(description = "사물함 목록") List<LockerResponse> lockers) {

    public static FloorWithLockersResponse of(
            Long floorId, Integer floorNumber, List<LockerResponse> lockers) {
        return new FloorWithLockersResponse(floorId, floorNumber, lockers);
    }
}
