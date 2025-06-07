package com.jnulocker.common.util;

import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionForConsumer {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeWithTransaction(Consumer<Void> consumer) {
        consumer.accept(null);
    }
}
