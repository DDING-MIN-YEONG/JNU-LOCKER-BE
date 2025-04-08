package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.exception.RoleNotCorrectException;
import com.jnulocker.auth.exception.UserAlreadyExistException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.organization.exception.OrganizationNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("동일한 메일에 대한 계정이 이미 존재할 때 발생하는 예외입니다.")
    public static final BusinessException 동일계정이_존재할_때 = UserAlreadyExistException.EXCEPTION;

    @ExplainError("단과대학 혹은 위원회가 존재하지 않을 때 발생하는 예외입니다.")
    public static final BusinessException 단과대학_혹은_위원회가_존재하지_않을_때 =
            OrganizationNotFoundException.EXCEPTION;

    @ExplainError("요청시 보낸 권한에 대한 접근이 부적절할 때 발생하는 예외입니다.")
    public static final BusinessException 권한에_대한_접근_부적절 = RoleNotCorrectException.EXCEPTION;
}
