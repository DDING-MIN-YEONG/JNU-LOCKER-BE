package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.BusinessException;

public class AiToolExecutionException extends BusinessException {

    public static final BusinessException EXCEPTION = new AiToolExecutionException();

    private AiToolExecutionException() {
        super(AiErrorCode.AI_TOOL_EXECUTION_ERROR);
    }
}
