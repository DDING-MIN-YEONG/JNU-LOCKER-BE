package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class LockerUnavailableException extends BusinessException {

    public static final BusinessException EXCEPTION = new LockerUnavailableException();

    private LockerUnavailableException() {
        super(EventErrorCode.LOCKER_UNAVAILABLE);
    }
}
