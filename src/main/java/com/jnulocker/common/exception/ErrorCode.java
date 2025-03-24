package com.jnulocker.common.exception;

import java.io.Serializable;
import org.springframework.http.HttpStatus;

public interface ErrorCode extends Serializable {

    String getCode();

    HttpStatus getHttpStatus();

    String getMessage();
}
