package com.jnulocker.auth.jwt.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {
    INVALID_ACCESS_TOKEN("JW001", HttpStatus.UNAUTHORIZED, "올바른 ACCESS 토큰이 아닙니다."),
    INVALID_REFRESH_TOKEN("JW002", HttpStatus.UNAUTHORIZED, "올바른 REFRESH 토큰이 아닙니다."),
    EXPIRED_TOKEN("JW003", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    AUTHENTICATION_FAIL("JW004", HttpStatus.UNAUTHORIZED, "Security Context 에 인증 정보가 없습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
