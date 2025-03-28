package com.jnulocker.member.domain.exception;

import com.jnulocker.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {
    INVALID_EMAIL("1001", HttpStatus.BAD_REQUEST, "유효하지 않은 전남대학교 메일입니다."),
    INVALID_PASSWORD("1002", HttpStatus.BAD_REQUEST, "올바르지 않은 비밀번호 형식입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
