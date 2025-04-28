package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class LockerNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new LockerNotFoundException();

    private LockerNotFoundException() {
        super(EventErrorCode.LOCKER_NOT_FOUND);
    }
}
