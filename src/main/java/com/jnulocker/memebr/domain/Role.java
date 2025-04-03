package com.jnulocker.memebr.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("guest"),
    MANAGER("manager"),
    ADMIN("admin"),
    USER("user");

    private final String role;
}
