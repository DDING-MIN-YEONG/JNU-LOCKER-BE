package com.jnulocker.events.application.port.in.response;

import java.util.List;

public record FloorWithLockersResponse(
        Long floorId, Integer floorNumber, List<LockerResponse> lockers) {

    public static FloorWithLockersResponse of(
            Long floorId, Integer floorNumber, List<LockerResponse> lockers) {
        return new FloorWithLockersResponse(floorId, floorNumber, lockers);
    }
}
