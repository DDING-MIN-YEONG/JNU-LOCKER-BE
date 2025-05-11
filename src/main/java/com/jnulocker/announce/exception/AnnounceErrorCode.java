package com.jnulocker.announce.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AnnounceErrorCode implements ErrorCode {
    ANNOUNCE_NOT_FOUND("AN001", HttpStatus.NOT_FOUND, "공지사항이 존재하지 않습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
