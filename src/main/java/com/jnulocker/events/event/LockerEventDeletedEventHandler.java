package com.jnulocker.events.event;

import com.jnulocker.events.quartz.QuartzSchedulerUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LockerEventDeletedEventHandler {

    private final QuartzSchedulerUtil quartzSchedulerUtil;

    @TransactionalEventListener(
            classes = LockerEventDeletedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void handle(LockerEventDeletedEvent lockerEventDeletedEvent) throws SchedulerException {
        UUID eventId = lockerEventDeletedEvent.getEventId();
        quartzSchedulerUtil.deleteEventJobs(eventId);
    }
}
