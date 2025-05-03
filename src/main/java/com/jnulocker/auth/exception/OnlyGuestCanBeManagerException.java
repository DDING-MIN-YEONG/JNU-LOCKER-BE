package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyGuestCanBeManagerException extends BusinessException {

    public static final BusinessException EXCEPTION = new OnlyGuestCanBeManagerException();

    private OnlyGuestCanBeManagerException() {
        super(AuthErrorCode.ONLY_GUEST_CAN_BE_MANAGER);
    }
}
