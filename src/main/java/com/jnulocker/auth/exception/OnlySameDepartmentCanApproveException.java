package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlySameDepartmentCanApproveException extends BusinessException {

    public static final BusinessException EXCEPTION = new OnlySameDepartmentCanApproveException();

    private OnlySameDepartmentCanApproveException() {
        super(AuthErrorCode.ONLY_SAME_DEPARTMENT_CAN_APPROVE);
    }
}
