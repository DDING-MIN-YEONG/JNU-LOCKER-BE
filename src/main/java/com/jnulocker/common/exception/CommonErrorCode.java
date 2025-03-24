package com.jnulocker.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INVALID_INPUT_VALUE("C001", HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
