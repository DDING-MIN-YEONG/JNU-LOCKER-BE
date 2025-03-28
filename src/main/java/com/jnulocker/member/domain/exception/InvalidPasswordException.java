package com.jnulocker.member.domain.exception;

import com.jnulocker.common.exception.BusinessException;

public class InvalidPasswordException extends BusinessException {
    public static final InvalidPasswordException EXCEPTION = new InvalidPasswordException();

    private InvalidPasswordException() {
        super(AuthErrorCode.INVALID_EMAIL);
    }
}
