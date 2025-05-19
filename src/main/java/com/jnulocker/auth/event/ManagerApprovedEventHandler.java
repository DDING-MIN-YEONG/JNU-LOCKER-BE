package com.jnulocker.auth.event;

import com.jnulocker.auth.application.port.out.ManagerEmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ManagerApprovedEventHandler {

    private final ManagerEmailSender managerEmailSender;

    @TransactionalEventListener(
            classes = ManagerApprovedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ManagerApprovedEvent event) {
        managerEmailSender.sendManagerApprovedEmail(event.getEmail(), event.getDepartment());
    }
}
