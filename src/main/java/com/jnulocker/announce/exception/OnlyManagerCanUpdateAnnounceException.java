package com.jnulocker.announce.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyManagerCanUpdateAnnounceException extends BusinessException {
    public static final BusinessException EXCEPTION = new OnlyManagerCanUpdateAnnounceException();

    private OnlyManagerCanUpdateAnnounceException() {
        super(AnnounceErrorCode.ONLY_MANAGER_CAN_UPDATE_ANNOUNCE);
    }
}
