package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.jwt.exception.JwtException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReissueExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("리프레시 토큰이 올바르지 않거나 만료되었을 때 발생하는 예외입니다.")
    public static final BusinessException 리프레시_토큰_오류 = JwtException.INVALID_REFRESH_TOKEN;
}
