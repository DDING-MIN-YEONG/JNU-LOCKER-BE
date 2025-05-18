package com.jnulocker.auth.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthUtil {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {
        String accessToken = CookieUtil.getCookieValueFromAccessToken(request);
        if (accessToken != null) {
            return accessToken;
        }

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length()).trim();
        }

        return null;
    }

    public static String getRefreshToken(HttpServletRequest request) {
        String refreshToken = CookieUtil.getCookieValueFromRefreshToken(request);
        if (refreshToken != null) {
            return refreshToken;
        }

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length()).trim();
        }

        return null;
    }
}
