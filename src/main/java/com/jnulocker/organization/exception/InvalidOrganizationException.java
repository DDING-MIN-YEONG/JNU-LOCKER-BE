package com.jnulocker.organization.exception;

import com.jnulocker.common.exception.BusinessException;

public class InvalidOrganizationException extends BusinessException {
    public static final BusinessException EXCEPTION = new InvalidOrganizationException();

    private InvalidOrganizationException() {
        super(OrganizationErrorCode.INVALID_ORGANIZATION);
    }
}
