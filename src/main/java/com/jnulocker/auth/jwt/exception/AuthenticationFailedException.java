package com.jnulocker.auth.jwt.exception;

import com.jnulocker.common.exception.BusinessException;

public class AuthenticationFailedException extends BusinessException {

    public static final BusinessException EXCEPTION = new AuthenticationFailedException();

    private AuthenticationFailedException() {
        super(JwtErrorCode.AUTHENTICATION_FAIL);
    }
}
