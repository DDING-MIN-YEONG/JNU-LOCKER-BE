package com.jnulocker.auth.event;

import com.jnulocker.auth.application.port.out.ManagerEmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ManagerRejectedEventHandler {

    private final ManagerEmailSender managerEmailSender;

    @TransactionalEventListener(
            classes = ManagerRejectedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ManagerRejectedEvent event) {
        managerEmailSender.sendManagerRejectedEmail(event.getEmail(), event.getDepartment());
    }
}
