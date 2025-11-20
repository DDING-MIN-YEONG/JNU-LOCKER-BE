package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class EmbeddingException extends BusinessException {

    public static final BusinessException GENERATION_FAILED =
            new EmbeddingException(AiErrorCode.EMBEDDING_GENERATION_FAILED);
    public static final BusinessException API_CLIENT_ERROR =
            new EmbeddingException(AiErrorCode.EMBEDDING_API_CLIENT_ERROR);
    public static final BusinessException API_SERVER_ERROR =
            new EmbeddingException(AiErrorCode.EMBEDDING_API_SERVER_ERROR);
    public static final BusinessException EMPTY_RESPONSE =
            new EmbeddingException(AiErrorCode.EMPTY_EMBEDDING_RESPONSE);
    public static final BusinessException INVALID_INPUT =
            new EmbeddingException(AiErrorCode.INVALID_EMBEDDING_INPUT);

    private EmbeddingException(AiErrorCode errorCode) {
        super(errorCode);
    }
}
