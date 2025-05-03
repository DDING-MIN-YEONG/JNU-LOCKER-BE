package com.jnulocker.member.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND("M001", HttpStatus.NOT_FOUND, "회원 정보를 조회할 수 없습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
