package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class UnauthorizedDocumentAccessException extends BusinessException {

    public static final BusinessException EXCEPTION = new UnauthorizedDocumentAccessException();

    private UnauthorizedDocumentAccessException() {
        super(AiErrorCode.UNAUTHORIZED_DOCUMENT_ACCESS);
    }
}
