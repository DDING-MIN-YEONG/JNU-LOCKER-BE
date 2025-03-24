package com.jnulocker.common.exception;

import com.jnulocker.common.response.ResponseEntityGenerator;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        ErrorCode errorCode = CommonErrorCode.INVALID_INPUT_VALUE;
        return makeErrorResponse(ex, errorCode);
    }

    private ResponseEntity<Object> makeErrorResponse(
            MethodArgumentNotValidException ex, ErrorCode errorCode) {
        List<ValidationError> invalidParams =
                ex.getBindingResult().getFieldErrors().stream().map(ValidationError::of).toList();
        return ResponseEntityGenerator.fail(errorCode, invalidParams);
    }
}
