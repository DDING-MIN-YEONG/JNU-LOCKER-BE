package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class StudentNumberRequiredException extends BusinessException {
    public static final BusinessException EXCEPTION = new StudentNumberRequiredException();

    private StudentNumberRequiredException() {
        super(AuthErrorCode.STUDENT_NUMBER_REQUIRED);
    }
}
