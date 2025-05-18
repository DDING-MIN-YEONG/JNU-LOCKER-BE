package com.jnulocker.auth.util;

import static com.jnulocker.auth.util.CookieUtil.*;
import static com.jnulocker.auth.util.CookieUtil.getCookieValueFromAccessToken;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthUtil {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {
        String accessToken = getCookieValueFromAccessToken(request);

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            accessToken = authHeader.substring(BEARER_PREFIX.length()).trim();
        }

        return accessToken;
    }

    public static String getRefreshToken(HttpServletRequest request) {
        String refreshToken = getCookieValueFromRefreshToken(request);

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            refreshToken = authHeader.substring(BEARER_PREFIX.length()).trim();
        }

        return refreshToken;
    }
}
