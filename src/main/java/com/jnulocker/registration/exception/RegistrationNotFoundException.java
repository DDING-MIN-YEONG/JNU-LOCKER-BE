package com.jnulocker.registration.exception;

import com.jnulocker.common.exception.BusinessException;

public class RegistrationNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new RegistrationNotFoundException();

    private RegistrationNotFoundException() {
        super(RegistrationErrorCode.REGISTRATION_NOT_FOUND);
    }
}
