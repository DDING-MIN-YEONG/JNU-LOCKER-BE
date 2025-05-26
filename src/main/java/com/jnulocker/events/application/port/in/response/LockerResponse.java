package com.jnulocker.events.application.port.in.response;

import com.jnulocker.events.domain.Locker;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record LockerResponse(
        @Schema(description = "사물함 ID", example = "550e8400-e29b-41d4-a716-446655440000")
                UUID lockerId,
        @Schema(description = "사물함 코드", example = "A101") String code,
        @Schema(description = "사물함 신청 가능 여부", example = "true") Boolean available) {

    public static LockerResponse from(Locker locker) {
        return new LockerResponse(locker.getId(), locker.getCode(), locker.getAvailable());
    }
}
