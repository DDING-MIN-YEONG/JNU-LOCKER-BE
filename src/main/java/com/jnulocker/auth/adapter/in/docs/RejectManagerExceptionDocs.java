package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.exception.OnlySameDepartmentCanApproveException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.member.exception.MemberNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RejectManagerExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("회원 정보를 조회할 수 없을 때 발생하는 예외입니다.")
    public static final BusinessException 회원_정보를_조회할_수_없을_때 = MemberNotFoundException.EXCEPTION;

    @ExplainError("같은 학과 소속이 아닐 때 발생하는 예외입니다.")
    public static final BusinessException 같은_학과_소속이_아닐_때 =
            OnlySameDepartmentCanApproveException.EXCEPTION;
}
