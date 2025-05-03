package com.jnulocker.auth.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyManagerCanApproveException extends BusinessException {

    public static final BusinessException EXCEPTION = new OnlyManagerCanApproveException();

    private OnlyManagerCanApproveException() {
        super(AuthErrorCode.ONLY_MANAGER_CAN_APPROVE);
    }
}
