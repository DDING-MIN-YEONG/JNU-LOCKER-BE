package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class SendEmailException extends BusinessException {
    public static final BusinessException EXCEPTION = new SendEmailException();

    private SendEmailException() {
        super(AuthErrorCode.SEND_EMAIL_ERROR);
    }
}
