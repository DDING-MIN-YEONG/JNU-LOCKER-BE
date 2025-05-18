package com.jnulocker.auth.jwt;

import static com.jnulocker.auth.util.CookieUtil.getCookieValueFromAccessToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.auth.exception.AuthErrorCode;
import com.jnulocker.auth.jwt.exception.InvalidAccessTokenException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.exception.ErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Value("${management.endpoints.web.base-path}")
    private String actuatorBasePath;

    private final TokenProvider tokenProvider;

    private List<String> EXCLUDE_URLS = new ArrayList<>();

    @PostConstruct
    public void init() {
        EXCLUDE_URLS =
                Arrays.asList(
                        "/v1/auth/*/signup",
                        "/v1/auth/login",
                        "/v1/auth/reissue",
                        "/v1/auth/send-email",
                        "/v1/auth/verify",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/api-docs/**",
                        "/v1/organizations",
                        "/v1/organizations/*/departments",
                        actuatorBasePath,
                        actuatorBasePath + "/health",
                        actuatorBasePath + "/prometheus");
    }

    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDE_URLS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
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

    private String getAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length()).trim();
        }

        return getCookieValueFromAccessToken(request);
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
