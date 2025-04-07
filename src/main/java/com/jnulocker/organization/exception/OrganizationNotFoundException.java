package com.jnulocker.organization.exception;

import com.jnulocker.common.exception.BusinessException;

public class OrganizationNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new OrganizationNotFoundException();

    private OrganizationNotFoundException() {
        super(OrganizationErrorCode.ORGANIZATION_NOT_FOUND);
    }
}
