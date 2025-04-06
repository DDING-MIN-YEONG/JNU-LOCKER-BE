package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    USER_ALREADY_EXIST("A001", HttpStatus.BAD_REQUEST, "이미 동일한 메일을 사용하는 계정이 존재합니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
