package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class ManagerAuthorizationRequiredException extends BusinessException {

    public static final BusinessException EXCEPTION = new ManagerAuthorizationRequiredException();

    private ManagerAuthorizationRequiredException() {
        super(AuthErrorCode.MANAGER_AUTHORIZATION_REQUIRED);
    }
}
