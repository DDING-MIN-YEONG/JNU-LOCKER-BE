package com.jnulocker.auth.jwt.exception;

import com.jnulocker.common.exception.BusinessException;

public class JwtException extends BusinessException {

    public static final BusinessException INVALID_ACCESS_TOKEN =
            new JwtException(JwtErrorCode.INVALID_ACCESS_TOKEN);
    public static final BusinessException INVALID_REFRESH_TOKEN =
            new JwtException(JwtErrorCode.INVALID_REFRESH_TOKEN);
    public static final BusinessException EXPIRED_TOKEN =
            new JwtException(JwtErrorCode.EXPIRED_TOKEN);
    public static final BusinessException AUTHENTICATION_FAIL =
            new JwtException(JwtErrorCode.AUTHENTICATION_FAIL);

    private JwtException(JwtErrorCode errorCode) {
        super(errorCode);
    }
}
