package com.jnulocker.auth.jwt;

import static com.jnulocker.auth.util.CookieUtil.getCookieValueFromAccessToken;

import com.jnulocker.auth.jwt.exception.InvalidAccessTokenException;
import com.jnulocker.common.exception.BusinessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    private final List<String> excludeUrls = Arrays.asList("/v1/auth/**");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return excludeUrls.stream()
                .anyMatch(
                        pattern -> {
                            if (pattern.endsWith("/**")) {
                                String basePath = pattern.substring(0, pattern.length() - 3);
                                return path.startsWith(basePath);
                            }
                            return path.equals(pattern);
                        });
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String accessToken = getCookieValueFromAccessToken(request);
            if (accessToken == null) {
                throw InvalidAccessTokenException.EXCEPTION;
            }
            if (tokenProvider.validateAccessToken(accessToken)) {
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            request.setAttribute("exception", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
