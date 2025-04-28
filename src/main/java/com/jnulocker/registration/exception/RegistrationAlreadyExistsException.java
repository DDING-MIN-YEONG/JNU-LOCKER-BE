package com.jnulocker.registration.exception;

import com.jnulocker.common.exception.BusinessException;

public class RegistrationAlreadyExistsException extends BusinessException {

    public static final BusinessException EXCEPTION = new RegistrationAlreadyExistsException();

    private RegistrationAlreadyExistsException() {
        super(RegistrationErrorCode.REGISTRATION_ALREADY_EXISTS);
    }
}
