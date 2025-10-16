package com.jnulocker.auth.security;

import com.jnulocker.auth.jwt.exception.JwtException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {
    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || authentication.getName() == null
                || !authentication.isAuthenticated()) {
            throw JwtException.AUTHENTICATION_FAIL;
        }
        return Long.valueOf(authentication.getName());
    }
}
