package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class EventNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new EventNotFoundException();

    private EventNotFoundException() {
        super(EventErrorCode.EVENT_NOT_FOUND);
    }
}
