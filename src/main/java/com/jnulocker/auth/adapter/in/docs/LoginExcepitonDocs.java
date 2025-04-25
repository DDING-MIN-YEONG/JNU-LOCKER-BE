package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.exception.FailAuthenticationException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginExcepitonDocs implements SwaggerExceptionDoc {

    @ExplainError("이메일/비밀번호가 올바르지 않을 때 발생하는 예외입니다.")
    public static final BusinessException 인증에_실패했을_때 = FailAuthenticationException.EXCEPTION;
}
