package com.jnulocker.auth.jwt;

import static com.jnulocker.auth.util.AuthUtil.getAccessToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.auth.exception.AuthErrorCode;
import com.jnulocker.auth.jwt.exception.InvalidAccessTokenException;
import com.jnulocker.auth.security.SecurityPaths;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.exception.ErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final SecurityPaths securityPaths;

    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return securityPaths.shouldNotFilter(request);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String accessToken = getAccessToken(request);
            if (accessToken == null) {
                throw InvalidAccessTokenException.EXCEPTION;
            }
            if (tokenProvider.validateAccessToken(accessToken)) {
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw InvalidAccessTokenException.EXCEPTION;
            }
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            log.error("JWT 처리 중 예외 발생: {}", e.getMessage());
            request.setAttribute("exception", e);
            setErrorResponse(response, e.getErrorCode());
        } catch (Exception e) {
            log.error("JWT 처리 중 예기치 않은 예외 발생: {}", e.getMessage());
            request.setAttribute("exception", e);
            setErrorResponse(response, AuthErrorCode.FAIL_AUTHENTICATION);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MEDIA_TYPE);

        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
