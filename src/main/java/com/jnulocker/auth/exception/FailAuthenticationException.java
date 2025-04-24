package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class FailAuthenticationException extends BusinessException {
    public static final BusinessException EXCEPTION = new FailAuthenticationException();
    private FailAuthenticationException() {
        super(AuthErrorCode.FAIL_AUTHENTICATION);
    }
}
