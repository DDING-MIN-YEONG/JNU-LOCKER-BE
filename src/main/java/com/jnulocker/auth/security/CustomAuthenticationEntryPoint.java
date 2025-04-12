package com.jnulocker.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.exception.ErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

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
            response.setContentType("application/json; charset=UTF-8");

            String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        }
    }
}
