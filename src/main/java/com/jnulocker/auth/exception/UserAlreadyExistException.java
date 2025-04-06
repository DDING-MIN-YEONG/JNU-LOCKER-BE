package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class UserAlreadyExistException extends BusinessException {
    public static final BusinessException EXCEPTION = new UserAlreadyExistException();

    public UserAlreadyExistException() {
        super(AuthErrorCode.USER_ALREADY_EXIST);
    }
}
