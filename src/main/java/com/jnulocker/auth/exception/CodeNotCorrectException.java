package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class CodeNotCorrectException extends BusinessException {
    public static final BusinessException EXCEPTION = new CodeNotCorrectException();

    private CodeNotCorrectException() {
        super(AuthErrorCode.CODE_NOT_CORRECT);
    }
}
