package com.jnulocker.organization.domain.model;

import java.util.UUID;

public record OrganizationId(UUID id) {
    public OrganizationId() {
        this(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
