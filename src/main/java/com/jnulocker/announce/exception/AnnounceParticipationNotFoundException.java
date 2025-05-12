package com.jnulocker.announce.exception;

import com.jnulocker.common.exception.BusinessException;

public class AnnounceParticipationNotFoundException extends BusinessException {
    public static final BusinessException EXCEPTION = new AnnounceParticipationNotFoundException();

    private AnnounceParticipationNotFoundException() {
        super(AnnounceErrorCode.ANNOUNCE_PARTICIPATION_NOT_FOUND);
    }
}
