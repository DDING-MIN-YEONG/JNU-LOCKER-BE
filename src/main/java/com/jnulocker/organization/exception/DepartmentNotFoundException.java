package com.jnulocker.organization.exception;

import com.jnulocker.common.exception.BusinessException;

public class DepartmentNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new DepartmentNotFoundException();

    private DepartmentNotFoundException() {
        super(DepartmentErrorCode.DEPARTMENT_NOT_FOUND);
    }
}
