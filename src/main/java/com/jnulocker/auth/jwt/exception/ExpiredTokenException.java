package com.jnulocker.auth.jwt.exception;

import com.jnulocker.common.exception.BusinessException;

public class ExpiredTokenException extends BusinessException {

    public static final BusinessException EXCEPTION = new ExpiredTokenException();

    private ExpiredTokenException() {
        super(JwtErrorCode.EXPIRED_TOKEN);
    }
}
