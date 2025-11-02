package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class FileSizeExceededException extends BusinessException {

    public static final BusinessException EXCEPTION = new FileSizeExceededException();

    private FileSizeExceededException() {
        super(AiErrorCode.FILE_SIZE_EXCEEDED);
    }
}
