package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class DocumentFileException extends BusinessException {

    public static final BusinessException FILE_SIZE_EXCEEDED =
            new DocumentFileException(AiErrorCode.FILE_SIZE_EXCEEDED);
    public static final BusinessException UNSUPPORTED_FILE_TYPE =
            new DocumentFileException(AiErrorCode.UNSUPPORTED_FILE_TYPE);

    private DocumentFileException(AiErrorCode errorCode) {
        super(errorCode);
    }
}
