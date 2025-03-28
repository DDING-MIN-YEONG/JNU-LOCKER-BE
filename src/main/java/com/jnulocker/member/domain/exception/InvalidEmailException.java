package com.jnulocker.member.domain.exception;

import com.jnulocker.common.exception.BusinessException;

public class InvalidEmailException extends BusinessException {
    public static final BusinessException EXCEPTION = new InvalidEmailException();

    public InvalidEmailException() {
        super(AuthErrorCode.INVALID_PASSWORD);
    }
}
