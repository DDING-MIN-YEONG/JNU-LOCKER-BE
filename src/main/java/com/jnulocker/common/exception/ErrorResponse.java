package com.jnulocker.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public record ErrorResponse(
        String code,
        Integer status,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) List<ValidationError> invalidParams) {

    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public ErrorResponse(ErrorCode errorCode, List<ValidationError> invalidParams) {
        this(
                errorCode.getCode(),
                errorCode.getHttpStatus().value(),
                errorCode.getMessage(),
                invalidParams);
    }
}
