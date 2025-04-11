package com.jnulocker.auth.util;

import com.jnulocker.auth.application.port.in.response.AuthToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseCookie;

@UtilityClass
public class CookieUtil {
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .secure(false)
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

    public static void addCookieFromAuthToken(HttpServletResponse response, AuthToken authToken) {
        addCookie(
                response,
                "access_token",
                authToken.accessToken(),
                Math.toIntExact(authToken.accessTokenExpiresIn()));
        addCookie(
                response,
                "refresh_token",
                authToken.refreshToken(),
                Math.toIntExact(authToken.refreshTokenExpiresIn()));
    }
}
