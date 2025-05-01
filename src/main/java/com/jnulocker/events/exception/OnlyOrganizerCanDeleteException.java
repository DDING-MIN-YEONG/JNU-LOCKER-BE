package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyOrganizerCanDeleteException extends BusinessException {

    public static final BusinessException EXCEPTION = new OnlyOrganizerCanDeleteException();

    private OnlyOrganizerCanDeleteException() {
        super(EventErrorCode.ONLY_ORGANIZER_CAN_DELETE);
    }
}
