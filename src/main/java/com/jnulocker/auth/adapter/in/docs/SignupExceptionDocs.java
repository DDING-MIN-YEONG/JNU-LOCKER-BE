package com.jnulocker.auth.adapter.in.docs;

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

    @ExplainError("회원가입시 입력받은 소속대학/소속학과와 일치한 정보를 찾지 못할 때 발생하는 예외입니다.")
    public static final BusinessException 소속대학_소속학과_불일치 = OrganizationNotFoundException.EXCEPTION;
}
