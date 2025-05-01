package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyManagerCanDeleteException extends BusinessException {

    public static final BusinessException EXCEPTION = new OnlyManagerCanDeleteException();

    private OnlyManagerCanDeleteException() {
        super(EventErrorCode.ONLY_MANAGER_CAN_DELETE);
    }
}
