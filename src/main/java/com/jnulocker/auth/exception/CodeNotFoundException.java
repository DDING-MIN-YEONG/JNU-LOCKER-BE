package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class CodeNotFoundException extends BusinessException {
    public static final BusinessException EXCEPTION = new CodeNotFoundException();

    private CodeNotFoundException() {
        super(AuthErrorCode.CODE_NOT_FOUND);
    }
}
