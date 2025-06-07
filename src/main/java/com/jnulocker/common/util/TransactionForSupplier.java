package com.jnulocker.common.util;

import java.util.function.Supplier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionForSupplier {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T executeWithTransaction(Supplier<T> supplier) {
        return supplier.get();
    }
}
