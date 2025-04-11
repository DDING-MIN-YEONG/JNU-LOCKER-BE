package com.jnulocker.auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }
        return Long.valueOf(authentication.getName());
    }
}
