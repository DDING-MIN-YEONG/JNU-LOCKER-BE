package com.jnulocker.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class SecurityPaths {

    public static final String[] PUBLIC_PATHS = {
        "/v1/auth/*/signup",
        "/v1/auth/login",
        "/v1/auth/reissue",
        "/v1/auth/send-email",
        "/v1/auth/verify",
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/api-docs/**",
        "/v3/api-docs/**"
    };

    public static final String[] PUBLIC_GET_PATHS = {
        "/v1/organizations", "/v1/organizations/*/departments"
    };

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${management.endpoints.web.base-path}")
    private String actuatorBasePath;

    public boolean shouldNotFilter(HttpServletRequest request) {
        String requestPath = request.getServletPath();
        String method = request.getMethod();

        if (matchesAnyPath(requestPath, PUBLIC_PATHS)) {
            return true;
        }

        if ("GET".equals(method) && matchesAnyPath(requestPath, PUBLIC_GET_PATHS)) {
            return true;
        }

        return isActuatorPath(requestPath);
    }

    public String[] getPublicPaths() {
        String[] actuatorPatterns = {
            actuatorBasePath, actuatorBasePath + "/health", actuatorBasePath + "/prometheus"
        };

        return Arrays.stream(new String[][] {PUBLIC_PATHS, actuatorPatterns})
                .flatMap(Arrays::stream)
                .toArray(String[]::new);
    }

    private boolean matchesAnyPath(String requestPath, String[] patterns) {
        return Arrays.stream(patterns).anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }

    private boolean isActuatorPath(String requestPath) {
        String[] actuatorPatterns = {
            actuatorBasePath, actuatorBasePath + "/health", actuatorBasePath + "/prometheus"
        };

        return Arrays.stream(actuatorPatterns)
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }
}
