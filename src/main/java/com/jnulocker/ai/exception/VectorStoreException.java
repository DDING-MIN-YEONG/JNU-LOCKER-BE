package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class VectorStoreException extends BusinessException {

    public static final BusinessException SAVE_FAILED =
            new VectorStoreException(AiErrorCode.VECTOR_STORE_SAVE_FAILED);
    public static final BusinessException DELETE_FAILED =
            new VectorStoreException(AiErrorCode.VECTOR_STORE_DELETE_FAILED);

    private VectorStoreException(AiErrorCode errorCode) {
        super(errorCode);
    }
}
