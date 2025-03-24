package com.jnulocker.common.response;

import org.springframework.http.HttpStatus;

public interface ApiResponseCode {
    String getMessage();
    HttpStatus getHttpStatus();
}
