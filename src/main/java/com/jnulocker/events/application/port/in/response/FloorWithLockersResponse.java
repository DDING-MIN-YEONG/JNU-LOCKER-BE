package com.jnulocker.events.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

public record FloorWithLockersResponse(
        @Schema(description = "층 ID", example = "550e8400-e29b-41d4-a716-446655440000")
                UUID floorId,
        @Schema(description = "층 번호", example = "1") Integer floorNumber,
        @Schema(description = "사물함 목록") List<LockerResponse> lockers) {

    public static FloorWithLockersResponse of(
            UUID floorId, Integer floorNumber, List<LockerResponse> lockers) {
        return new FloorWithLockersResponse(floorId, floorNumber, lockers);
    }
}
