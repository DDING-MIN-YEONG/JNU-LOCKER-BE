package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class RoleNotCorrectException extends BusinessException {
    public static final BusinessException EXCEPTION = new RoleNotCorrectException();

    public RoleNotCorrectException() {
        super(AuthErrorCode.ROLE_NOT_CORRECT);
    }
}
