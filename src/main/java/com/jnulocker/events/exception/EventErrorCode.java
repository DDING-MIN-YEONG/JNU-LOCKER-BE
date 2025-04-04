package com.jnulocker.events.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventErrorCode implements ErrorCode {
    EVENT_NOT_FOUND("E001", HttpStatus.NOT_FOUND, "이벤트가 존재하지 않습니다"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
