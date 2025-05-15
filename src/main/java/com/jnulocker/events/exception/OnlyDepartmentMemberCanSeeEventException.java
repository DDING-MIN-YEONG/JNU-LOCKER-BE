package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyDepartmentMemberCanSeeEventException extends BusinessException {

    public static final BusinessException EXCEPTION =
            new OnlyDepartmentMemberCanSeeEventException();

    private OnlyDepartmentMemberCanSeeEventException() {
        super(EventErrorCode.ONLY_DEPARTMENT_MEMBER_CAN_SEE_EVENT);
    }
}
