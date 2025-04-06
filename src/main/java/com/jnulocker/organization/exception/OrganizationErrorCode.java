package com.jnulocker.organization.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrganizationErrorCode implements ErrorCode {
    ORGANIZATION_NOT_FOUND("O001", HttpStatus.NOT_FOUND, "알맞는 소속대학/소속학과가 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
