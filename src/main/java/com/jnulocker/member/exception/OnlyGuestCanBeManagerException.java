package com.jnulocker.member.exception;

import com.jnulocker.common.exception.BusinessException;

public class OnlyGuestCanBeManagerException extends BusinessException {

    public static final BusinessException EXCEPTION = new OnlyGuestCanBeManagerException();

    private OnlyGuestCanBeManagerException() {
        super(MemberErrorCode.ONLY_GUEST_CAN_BE_MANAGER);
    }
}
