package com.jnulocker.organization.domain.model;

import java.util.concurrent.atomic.AtomicLong;

public record OrganizationId(Long id) {
    private static final AtomicLong counter = new AtomicLong(1);

    public OrganizationId() {
        this(counter.getAndIncrement());
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
