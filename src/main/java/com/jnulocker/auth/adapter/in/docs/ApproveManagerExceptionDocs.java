package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.exception.OnlyManagerCanApproveException;
import com.jnulocker.auth.exception.OnlySameDepartmentCanApproveException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.events.exception.EventNotFoundException;
import com.jnulocker.member.exception.MemberNotFoundException;
import com.jnulocker.member.exception.OnlyGuestCanBeManagerException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApproveManagerExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("회원 정보를 조회할 수 없을 때 발생하는 예외입니다.")
    public static final BusinessException 회원_정보를_조회할_수_없을_때 = MemberNotFoundException.EXCEPTION;

    @ExplainError("이벤트가 존재하지 않을 때 발생하는 예외입니다.")
    public static final BusinessException 이벤트가_존재하지_않을_때 = EventNotFoundException.EXCEPTION;

    @ExplainError("이벤트 관리자(MANAGER)가 아닐 때 발생하는 예외입니다.")
    public static final BusinessException 이벤트_관리자가_아닐_때 = OnlyManagerCanApproveException.EXCEPTION;

    @ExplainError("같은 학과 소속이 아닐 때 발생하는 예외입니다.")
    public static final BusinessException 같은_학과_소속이_아닐_때 =
            OnlySameDepartmentCanApproveException.EXCEPTION;

    @ExplainError("승인 받는 사람이 GUEST가 아닐 때 발생하는 예외입니다.")
    public static final BusinessException 승인_받는_사람이_GUEST가_아닐_때 =
            OnlyGuestCanBeManagerException.EXCEPTION;
}
