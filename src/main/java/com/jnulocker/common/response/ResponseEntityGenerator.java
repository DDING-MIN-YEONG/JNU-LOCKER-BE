package com.jnulocker.common.response;

import com.jnulocker.common.exception.ErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.common.exception.ValidationError;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseEntityGenerator {
    public static <D> ResponseEntity<D> success(D data, HttpStatus status) {
        return ResponseEntity.status(status).body(data);
    }

    public static ResponseEntity<Void> success(HttpStatus status) {
        return ResponseEntity.status(status).build();
    }

    public static ResponseEntity<ErrorResponse> fail(ErrorCode errorCode) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    public static ResponseEntity<Object> fail(
            ErrorCode errorCode, List<ValidationError> invalidParams) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResponse(errorCode, invalidParams));
    }
}
