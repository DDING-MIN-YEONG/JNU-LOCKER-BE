package com.jnulocker.member.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    ONLY_GUEST_CAN_BE_MANAGER("M001", HttpStatus.BAD_REQUEST, "게스트만 매니저로 승인될 수 있습니다"),
    MEMBER_NOT_FOUND("M002", HttpStatus.NOT_FOUND, "회원 정보를 조회할 수 없습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
