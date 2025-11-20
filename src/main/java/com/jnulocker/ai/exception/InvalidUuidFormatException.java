package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class InvalidUuidFormatException extends BusinessException {

    public static final BusinessException EXCEPTION = new InvalidUuidFormatException();

    private InvalidUuidFormatException() {
        super(AiErrorCode.INVALID_UUID_FORMAT);
    }
}
