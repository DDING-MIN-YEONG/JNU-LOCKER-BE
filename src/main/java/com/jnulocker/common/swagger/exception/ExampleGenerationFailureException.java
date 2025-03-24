package com.jnulocker.common.swagger.exception;

import com.jnulocker.common.exception.BusinessException;

public class ExampleGenerationFailureException extends BusinessException {

    public static final BusinessException EXCEPTION = new ExampleGenerationFailureException();

    private ExampleGenerationFailureException() {
        super(SwaggerErrorCode.EXAMPLE_GENERATION_FAILURE);
    }
}
