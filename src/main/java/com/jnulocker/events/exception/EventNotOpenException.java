package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class EventNotOpenException extends BusinessException {

    public static final BusinessException EXCEPTION = new EventNotOpenException();

    private EventNotOpenException() {
        super(EventErrorCode.EVENT_NOT_OPEN);
    }
}
