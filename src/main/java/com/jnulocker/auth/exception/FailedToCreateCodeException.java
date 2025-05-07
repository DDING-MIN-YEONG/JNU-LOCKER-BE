package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class FailedToCreateCodeException extends BusinessException {

    public static final BusinessException EXCEPTION = new FailedToCreateCodeException();

    private FailedToCreateCodeException() {
        super(AuthErrorCode.FAILED_TO_CREATE_CODE);
    }
}
