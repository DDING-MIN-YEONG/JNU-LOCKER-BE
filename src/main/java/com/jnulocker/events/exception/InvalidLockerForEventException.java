package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class InvalidLockerForEventException extends BusinessException {

    public static final BusinessException EXCEPTION = new InvalidLockerForEventException();

    private InvalidLockerForEventException() {
        super(EventErrorCode.INVALID_LOCKER_FOR_EVENT);
    }
}
