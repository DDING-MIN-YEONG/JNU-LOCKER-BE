package com.jnulocker.announce.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyManagerCanDeleteAnnounceException extends BusinessException {

    public static final BusinessException EXCEPTION = new OnlyManagerCanDeleteAnnounceException();

    private OnlyManagerCanDeleteAnnounceException() {
        super(AnnounceErrorCode.ONLY_MANAGER_CAN_DELETE_ANNOUNCE);
    }
}
