package com.jnulocker.member.domain.model;

import java.util.concurrent.atomic.AtomicLong;

public record MemberId(Long id) {
    private static final AtomicLong counter = new AtomicLong(1);

    public MemberId() {
        this(counter.getAndIncrement());
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
