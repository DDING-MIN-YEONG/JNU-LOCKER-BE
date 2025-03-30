package com.jnulocker.member.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthInfo {
    private final String email;
    private final String password;

    public static AuthInfo create(String email, String password) {
        return new AuthInfo(email, password);
    }

    public static AuthInfo load(String email, String password) {
        return new AuthInfo(email, password);
    }
}
