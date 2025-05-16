package com.jnulocker.events.event;

import com.jnulocker.events.quartz.QuartzSchedulerUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class LockerEventCreatedEventHandler {

    private final QuartzSchedulerUtil quartzSchedulerUtil;

    @TransactionalEventListener(
            classes = LockerEventCreatedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void handle(LockerEventCreatedEvent lockerEventCreatedEvent) {
        UUID eventId = lockerEventCreatedEvent.getEventId();
        quartzSchedulerUtil.scheduleEventJobs(
                eventId, lockerEventCreatedEvent.getStartAt(), lockerEventCreatedEvent.getEndAt());
    }
}
