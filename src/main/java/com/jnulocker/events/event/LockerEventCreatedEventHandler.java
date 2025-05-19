package com.jnulocker.events.event;

import com.jnulocker.events.quartz.QuartzSchedulerUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LockerEventCreatedEventHandler {

    private final QuartzSchedulerUtil quartzSchedulerUtil;

    @TransactionalEventListener(
            classes = LockerEventCreatedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void handle(LockerEventCreatedEvent event) {
        UUID eventId = event.getEventId();
        quartzSchedulerUtil.scheduleEventJobs(eventId, event.getStartAt(), event.getEndAt());
    }
}
