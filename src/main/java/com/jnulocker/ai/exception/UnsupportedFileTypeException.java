package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class UnsupportedFileTypeException extends BusinessException {

    public static final BusinessException EXCEPTION = new UnsupportedFileTypeException();

    private UnsupportedFileTypeException() {
        super(AiErrorCode.UNSUPPORTED_FILE_TYPE);
    }
}
