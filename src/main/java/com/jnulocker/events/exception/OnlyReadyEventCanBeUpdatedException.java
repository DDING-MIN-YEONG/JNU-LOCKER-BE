package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyReadyEventCanBeUpdatedException extends BusinessException {

    public static final BusinessException EXCEPTION = new OnlyReadyEventCanBeUpdatedException();

    private OnlyReadyEventCanBeUpdatedException() {
        super(EventErrorCode.ONLY_READY_EVENT_CAN_BE_UPDATED);
    }
}
