package com.jnulocker.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.auth.exception.AuthErrorCode;
import com.jnulocker.common.exception.ErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/*
인가를 처리함 - 알맞지 않는 권한으로 요청을 보낼 시
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {
        ErrorCode errorCode = AuthErrorCode.FAIL_AUTHORIZATION;
        ErrorResponse errorResponse = new ErrorResponse(errorCode);

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MEDIA_TYPE);

        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
