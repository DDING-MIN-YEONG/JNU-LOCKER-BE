package com.jnulocker.organization.exception;

import com.jnulocker.common.exception.BusinessException;

public class DepartmentNotFoundException extends BusinessException {
    public static final BusinessException EXCEPTION = new DepartmentNotFoundException();
    public DepartmentNotFoundException() {
        super(OrganizationErrorCode.ORGANIZATION_NOT_FOUND);
    }
}
