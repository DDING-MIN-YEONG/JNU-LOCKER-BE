package com.jnulocker.auth.util;

import com.jnulocker.auth.application.port.in.response.AuthToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseCookie;

@UtilityClass
public class CookieUtil {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie =
                ResponseCookie.from(name, value)
                        .path("/")
                        .sameSite("None")
                        .httpOnly(true)
                        .secure(true)
                        .maxAge(maxAge)
                        .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public static String getCookieValueFromAccessToken(HttpServletRequest request) {
        return getCookieValue(request, ACCESS_TOKEN);
    }

    public static String getCookieValueFromRefreshToken(HttpServletRequest request) {
        return getCookieValue(request, REFRESH_TOKEN);
    }

    public static void addCookieFromAuthToken(HttpServletResponse response, AuthToken authToken) {
        addCookie(
                response,
                ACCESS_TOKEN,
                authToken.accessToken(),
                Math.toIntExact(authToken.accessTokenExpiresIn()));
        addCookie(
                response,
                REFRESH_TOKEN,
                authToken.refreshToken(),
                Math.toIntExact(authToken.refreshTokenExpiresIn()));
    }

    public static void clearAuthCookies(HttpServletResponse response) {
        addCookie(response, ACCESS_TOKEN, null, 0);
        addCookie(response, REFRESH_TOKEN, null, 0);
    }
}
