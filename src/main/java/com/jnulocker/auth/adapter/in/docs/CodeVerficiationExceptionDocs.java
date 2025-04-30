package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.exception.CodeExpiredException;
import com.jnulocker.auth.exception.CodeNotCorrectException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeVerficiationExceptionDocs implements SwaggerExceptionDoc {
    @ExplainError("인증코드가 일치하지 않을 때 발생하는 예외입니다.")
    public static final BusinessException 인증코드가_일치하지_않을_때 = CodeNotCorrectException.EXCEPTION;

    @ExplainError("인증코드가 유효시간이 지나 만료되었을 때 발생하는 예외입니다.")
    public static final BusinessException 인증코드가_만료되었을_때 = CodeExpiredException.EXCEPTION;
}
