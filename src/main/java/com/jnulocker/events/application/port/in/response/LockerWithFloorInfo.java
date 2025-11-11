package com.jnulocker.events.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record LockerWithFloorInfo(
        @Schema(description = "사물함 ID", example = "550e8400-e29b-41d4-a716-446655440000")
                UUID lockerId,
        @Schema(description = "사물함 코드", example = "A301") String code,
        @Schema(description = "층 번호", example = "3") Integer floorNumber) {

    public static LockerWithFloorInfo of(UUID lockerId, String code, Integer floorNumber) {
        return new LockerWithFloorInfo(lockerId, code, floorNumber);
    }
}
