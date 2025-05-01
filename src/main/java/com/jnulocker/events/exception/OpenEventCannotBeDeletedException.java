package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class OpenEventCannotBeDeletedException extends BusinessException {

    public static final BusinessException EXCEPTION = new OpenEventCannotBeDeletedException();

    private OpenEventCannotBeDeletedException() {
        super(EventErrorCode.OPEN_EVENT_CAN_NOT_BE_DELETED);
    }
}
