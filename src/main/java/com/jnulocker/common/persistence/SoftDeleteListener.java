package com.jnulocker.common.persistence;

import jakarta.persistence.PreRemove;

public class SoftDeleteListener {
    @PreRemove
    private void preRemove(BaseEntity entity) {
        entity.delete();
    }
}
