package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class DocumentNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new DocumentNotFoundException();

    private DocumentNotFoundException() {
        super(AiErrorCode.DOCUMENT_NOT_FOUND);
    }
}
