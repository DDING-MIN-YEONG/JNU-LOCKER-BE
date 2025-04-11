package com.jnulocker.auth.jwt.exception;

import com.jnulocker.common.exception.BusinessException;

public class MissingTokenException extends BusinessException {

    public static final BusinessException EXCEPTION = new MissingTokenException();

    private MissingTokenException() {
        super(JwtErrorCode.MISSING_TOKEN);
    }
}
