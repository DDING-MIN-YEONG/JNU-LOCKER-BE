package com.jnulocker.auth.jwt.exception;

import com.jnulocker.common.exception.BusinessException;

public class InvalidAccessTokenException extends BusinessException {

    public static final BusinessException EXCEPTION = new InvalidAccessTokenException();

    private InvalidAccessTokenException() {
        super(JwtErrorCode.INVALID_ACCESS_TOKEN);
    }
}
