package com.jnulocker.events.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventErrorCode implements ErrorCode {
    EVENT_NOT_FOUND("E001", HttpStatus.NOT_FOUND, "이벤트가 존재하지 않습니다"),
    EVENT_NOT_OPEN("E002", HttpStatus.BAD_REQUEST, "이벤트가 열려있지 않습니다"),
    LOCKER_NOT_FOUND("E003", HttpStatus.NOT_FOUND, "사물함이 존재하지 않습니다"),
    LOCKER_UNAVAILABLE("E004", HttpStatus.BAD_REQUEST, "사용중인 사물함입니다"),
    INVALID_LOCKER_FOR_EVENT("E005", HttpStatus.BAD_REQUEST, "이벤트에 해당하지 않는 사물함입니다"),
    ONLY_ORGANIZER_CAN_DELETE("E006", HttpStatus.FORBIDDEN, "이벤트 관리자만 삭제할 수 있습니다"),
    OPEN_EVENT_CAN_NOT_BE_DELETED("E007", HttpStatus.BAD_REQUEST, "진행중인 이벤트는 삭제할 수 없습니다"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
