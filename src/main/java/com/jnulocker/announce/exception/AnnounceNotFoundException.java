package com.jnulocker.announce.exception;

import com.jnulocker.common.exception.BusinessException;

public class AnnounceNotFoundException extends BusinessException {
    public static final BusinessException EXCEPTION = new AnnounceNotFoundException();

    public AnnounceNotFoundException() {
        super(AnnounceErrorCode.ANNOUNCE_NOT_FOUND);
    }
}
