package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class EmptyFileException extends BusinessException {

    public static final BusinessException EXCEPTION = new EmptyFileException();

    private EmptyFileException() {
        super(AiErrorCode.EMPTY_FILE);
    }
}
