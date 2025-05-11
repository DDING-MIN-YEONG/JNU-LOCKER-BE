package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyManagerCanDeleteEventException extends BusinessException {

    public static final BusinessException EXCEPTION = new OnlyManagerCanDeleteEventException();

    private OnlyManagerCanDeleteEventException() {
        super(EventErrorCode.ONLY_MANAGER_CAN_DELETE_EVENT);
    }
}
