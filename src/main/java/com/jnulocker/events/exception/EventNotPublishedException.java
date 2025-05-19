package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class EventNotPublishedException extends BusinessException {

    public static final BusinessException EXCEPTION = new EventNotPublishedException();

    private EventNotPublishedException() {
        super(EventErrorCode.EVENT_NOT_PUBLISHED);
    }
}
