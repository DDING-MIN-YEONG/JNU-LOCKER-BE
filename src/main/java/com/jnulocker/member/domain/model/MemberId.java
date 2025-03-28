package com.jnulocker.member.domain.model;

import java.util.UUID;

public record MemberId(UUID id) {
    public MemberId() {
        this(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
