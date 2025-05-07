package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyParticipationDepartmentCanRegisterException extends BusinessException {

    public static final BusinessException EXCEPTION =
            new OnlyParticipationDepartmentCanRegisterException();

    private OnlyParticipationDepartmentCanRegisterException() {
        super(EventErrorCode.ONLY_PARTICIPATION_DEPARTMENT_CAN_REGISTER);
    }
}
