package com.jnulocker.member.domain.model;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER"),
    GUEST("GUEST"),
    MANAGER("MANAGER"),
    ADMIN("ADMIN"),
    ;

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
