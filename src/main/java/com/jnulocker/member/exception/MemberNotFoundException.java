package com.jnulocker.member.exception;

import com.jnulocker.common.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new MemberNotFoundException();

    private MemberNotFoundException() {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}
