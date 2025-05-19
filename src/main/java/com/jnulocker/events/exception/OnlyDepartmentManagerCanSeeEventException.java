package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyDepartmentManagerCanSeeEventException extends BusinessException {

    public static final BusinessException EXCEPTION =
            new OnlyDepartmentManagerCanSeeEventException();

    private OnlyDepartmentManagerCanSeeEventException() {
        super(EventErrorCode.ONLY_DEPARTMENT_MANAGER_CAN_SEE_EVENT);
    }
}
