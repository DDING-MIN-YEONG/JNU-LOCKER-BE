package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class EmailNotVerifiedException extends BusinessException {
    public static final BusinessException EXCEPTION = new EmailNotVerifiedException();

    private EmailNotVerifiedException() {
        super(AuthErrorCode.EMAIL_NOT_VERIFIED);
    }
}
