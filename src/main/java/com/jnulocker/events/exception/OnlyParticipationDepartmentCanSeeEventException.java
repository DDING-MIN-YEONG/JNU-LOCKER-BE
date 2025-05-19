package com.jnulocker.events.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyParticipationDepartmentCanSeeEventException extends BusinessException {

    public static final BusinessException EXCEPTION =
            new OnlyParticipationDepartmentCanSeeEventException();

    private OnlyParticipationDepartmentCanSeeEventException() {
        super(EventErrorCode.ONLY_PARTICIPATION_DEPARTMENT_CAN_SEE_EVENT);
    }
}
