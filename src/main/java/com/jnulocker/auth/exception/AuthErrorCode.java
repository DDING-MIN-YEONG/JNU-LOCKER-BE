package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    USER_ALREADY_EXIST("A001", HttpStatus.CONFLICT, "이미 동일한 메일을 사용하는 계정이 존재합니다."),
    FAIL_AUTHENTICATION("A002", HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    FAIL_AUTHORIZATION("A003", HttpStatus.FORBIDDEN, "권한이 없습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
