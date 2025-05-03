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
    STUDENT_NUMBER_REQUIRED("A004", HttpStatus.BAD_REQUEST, "학생회 회원가입 시 학번은 필수입력사항입니다."),
    SEND_EMAIL_ERROR("A005", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 중 오류가 발생했습니다."),
    CODE_NOT_CORRECT("A006", HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
    CODE_NOT_FOUND("A007", HttpStatus.BAD_REQUEST, "인증 요청 정보가 존재하지 않습니다. 재요청 후 5분 이내로 인증해주세요"),
    EMAIL_NOT_VERIFIED("A008", HttpStatus.BAD_REQUEST, "이메일 인증이 완료되지 않았습니다."),
    ONLY_MANAGER_CAN_APPROVE("A009", HttpStatus.FORBIDDEN, "관리자만 승인할 수 있습니다."),
    ONLY_SAME_DEPARTMENT_CAN_APPROVE("A010", HttpStatus.FORBIDDEN, "같은 학과의 관리자만 승인할 수 있습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
