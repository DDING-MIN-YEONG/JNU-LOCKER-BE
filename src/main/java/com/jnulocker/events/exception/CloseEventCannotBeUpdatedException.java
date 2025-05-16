package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class CloseEventCannotBeUpdatedException extends BusinessException {

    public static final BusinessException EXCEPTION = new CloseEventCannotBeUpdatedException();

    private CloseEventCannotBeUpdatedException() {
        super(EventErrorCode.CLOSE_EVENT_CAN_NOT_BE_UPDATED);
    }
}
