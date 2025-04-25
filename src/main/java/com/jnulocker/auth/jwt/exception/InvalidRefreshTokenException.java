package com.jnulocker.auth.jwt.exception;

import com.jnulocker.common.exception.BusinessException;

public class InvalidRefreshTokenException extends BusinessException {

    public static final BusinessException EXCEPTION = new InvalidRefreshTokenException();

    private InvalidRefreshTokenException() {
        super(JwtErrorCode.INVALID_REFRESH_TOKEN);
    }
}
