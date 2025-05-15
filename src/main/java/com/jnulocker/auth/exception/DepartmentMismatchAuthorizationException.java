package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class DepartmentMismatchAuthorizationException extends BusinessException {

    public static final BusinessException EXCEPTION = new DepartmentMismatchAuthorizationException();

    private DepartmentMismatchAuthorizationException() {
        super(AuthErrorCode.DEPARTMENT_MISMATCH_AUTHORIZATION);
    }
}
