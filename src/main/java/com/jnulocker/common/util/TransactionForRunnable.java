package com.jnulocker.common.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionForRunnable {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeWithTransaction(Runnable runnable) {
        runnable.run();
    }
}
