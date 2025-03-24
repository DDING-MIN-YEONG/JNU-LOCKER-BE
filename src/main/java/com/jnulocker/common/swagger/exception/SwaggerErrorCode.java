package com.jnulocker.common.swagger.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SwaggerErrorCode implements ErrorCode {
    EXAMPLE_GENERATION_FAILURE(
            "S001", HttpStatus.INTERNAL_SERVER_ERROR, "Swagger 예제 생성 중 오류가 발생했습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
