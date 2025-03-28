package com.jnulocker.member.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.jnulocker.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_EMAIL("A001", HttpStatus.BAD_REQUEST, "유효하지 않은 전남대학교 메일입니다."),
    INVALID_PASSWORD("A002", HttpStatus.BAD_REQUEST, "올바르지 않은 비밀번호 형식입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
