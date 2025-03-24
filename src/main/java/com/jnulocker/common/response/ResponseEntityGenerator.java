package com.jnulocker.common.response;

import com.jnulocker.common.exception.ErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.common.exception.ValidationError;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityGenerator {
    public static <D> ResponseEntity<SuccessBody<D>> success(D data, HttpStatus status) {
        return new ResponseEntity<>(
                new SuccessBody<>(data), status);
    }

    public static ResponseEntity<SuccessBody<Void>> success(HttpStatus status) {
        return success(null, status);
    }

//    public static ResponseEntity<Object> fail(ErrorCode errorCode, List<ValidationError> invalidParams) {
//        return ResponseEntity
//                .status(errorCode.getHttpStatus())
//                .body(new ErrorResponse(errorCode, invalidParams));
//    }
//
//    public static ResponseEntity<ErrorResponse> fail(ErrorCode errorCode) {
//        return fail(errorCode, null);
//    }
    public static ResponseEntity<ErrorResponse> fail(ErrorCode errorCode) {
        return fail(errorCode, null);
    }

    public static ResponseEntity<ErrorResponse> fail(ErrorCode errorCode, List<ValidationError> invalidParams) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, invalidParams);
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

}
