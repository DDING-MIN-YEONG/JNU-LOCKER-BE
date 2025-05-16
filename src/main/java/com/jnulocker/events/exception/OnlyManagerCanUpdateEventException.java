package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyManagerCanUpdateEventException extends BusinessException {

    public static final BusinessException EXCEPTION = new OnlyManagerCanUpdateEventException();

    private OnlyManagerCanUpdateEventException() {
        super(EventErrorCode.ONLY_MANAGER_CAN_UPDATE_EVENT);
    }
}
