package com.jnulocker.events.application.port.in.response;

import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

public record FloorWithLockersResponse(
        @Schema(description = "층 ID", example = "550e8400-e29b-41d4-a716-446655440000")
                UUID floorId,
        @Schema(description = "층 번호", example = "1") Integer floorNumber,
        @Schema(description = "사물함 목록") List<LockerResponse> lockers) {

    public static FloorWithLockersResponse of(Floor floor, List<Locker> lockers) {
        List<LockerResponse> lockerResponses = lockers.stream().map(LockerResponse::from).toList();

        return new FloorWithLockersResponse(floor.getId(), floor.getFloorNumber(), lockerResponses);
    }
}
