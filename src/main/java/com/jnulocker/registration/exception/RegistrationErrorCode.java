package com.jnulocker.registration.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RegistrationErrorCode implements ErrorCode {
    REGISTRATION_ALREADY_EXISTS("R001", HttpStatus.BAD_REQUEST, "이미 해당 회원이 신청한 사물함이 존재합니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
