package com.jnulocker.organization.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrganizationErrorCode implements ErrorCode {
    ORGANIZATION_NOT_FOUND("O001", HttpStatus.NOT_FOUND, "단과대학 혹은 위원회가 존재하지 않습니다"),
    DEPARTMENT_NOT_FOUND("O001", HttpStatus.NOT_FOUND, "존재하지 않은 학과입니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
