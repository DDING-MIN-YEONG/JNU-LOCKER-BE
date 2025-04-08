package com.jnulocker.organization.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DepartmentErrorCode implements ErrorCode {
    DEPARTMENT_NOT_FOUND("D001", HttpStatus.NOT_FOUND, "학과가 존재하지 않습니다"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
