package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.exception.CodeNotCorrectException;
import com.jnulocker.auth.exception.CodeNotFoundException;
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

    @ExplainError("인증코드를 찾을 수 없을 때 발생하는 예외입니다.")
    public static final BusinessException 인증코드를_찾을_수_없을_때 = CodeNotFoundException.EXCEPTION;
}
