package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class DocumentParseFailedException extends BusinessException {

    public static final BusinessException EXCEPTION = new DocumentParseFailedException();

    private DocumentParseFailedException() {
        super(AiErrorCode.DOCUMENT_PARSE_FAILED);
    }
}
