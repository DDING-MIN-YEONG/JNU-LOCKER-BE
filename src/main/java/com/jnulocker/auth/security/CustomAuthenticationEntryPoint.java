package com.jnulocker.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.auth.exception.AuthErrorCode;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.exception.ErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/*
인증을 처리함 - 로그인, 회원가입
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {
        Throwable cause = (Throwable) request.getAttribute("exception");

        if (cause instanceof BusinessException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ErrorResponse errorResponse = new ErrorResponse(errorCode);

            response.setStatus(errorCode.getHttpStatus().value());
            response.setContentType(MEDIA_TYPE);

            String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        } else {
            ErrorCode errorCode = AuthErrorCode.FAIL_AUTHENTICATION;
            ErrorResponse errorResponse = new ErrorResponse(errorCode);

            response.setStatus(errorCode.getHttpStatus().value());
            response.setContentType(MEDIA_TYPE);

            String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        }
    }
}
