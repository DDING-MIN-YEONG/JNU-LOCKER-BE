package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class CodeExpiredException extends BusinessException {
    public static final BusinessException EXCEPTION = new CodeExpiredException();

    private CodeExpiredException() {
        super(AuthErrorCode.CODE_EXPIRED);
    }
}
