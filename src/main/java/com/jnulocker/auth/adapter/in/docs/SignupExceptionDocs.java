package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.exception.UserAlreadyExistException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.organization.exception.DepartmentNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("동일한 메일에 대한 계정이 이미 존재할 때 발생하는 예외입니다.")
    public static final BusinessException 동일한_메일을_사용하는_계정이_존재할_때 =
            UserAlreadyExistException.EXCEPTION;

    @ExplainError("학과가 존재하지 않을 때 발생하는 예외입니다.")
    public static final BusinessException 학과가_존재하지_않을_때 = DepartmentNotFoundException.EXCEPTION;
}
