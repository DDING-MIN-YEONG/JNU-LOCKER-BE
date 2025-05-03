package com.jnulocker.auth.security;

import com.jnulocker.auth.jwt.exception.AuthenticationFailedException;
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
            throw AuthenticationFailedException.EXCEPTION;
        }
        return Long.valueOf(authentication.getName());
    }
}
