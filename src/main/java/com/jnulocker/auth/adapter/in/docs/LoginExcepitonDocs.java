package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.jwt.exception.ExpiredTokenException;
import com.jnulocker.auth.jwt.exception.InvalidRefreshTokenException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginExcepitonDocs implements SwaggerExceptionDoc {

    @ExplainError("refreshToken이 올바르지 않을 때 발생하는 예외입니다.")
    public static final BusinessException 리프레스_토큰이_올바르지_않을_때 =
            InvalidRefreshTokenException.EXCEPTION;

    @ExplainError("토큰이 만료되었을 때 발생하는 예외입니다.")
    public static final BusinessException 토큰이_만료되었을_때 = ExpiredTokenException.EXCEPTION;
}
