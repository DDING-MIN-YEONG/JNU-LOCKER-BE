package com.jnulocker.events.application.port.in.response;

import com.jnulocker.events.domain.Locker;

public record LockerResponse(Long lockerId, String code, Boolean available) {

    public static LockerResponse from(Locker locker) {
        return new LockerResponse(locker.getId(), locker.getCode(), locker.getAvailable());
    }
}
