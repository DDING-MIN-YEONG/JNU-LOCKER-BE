package com.jnulocker.events.event;

import lombok.RequiredArgsConstructor;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LockerEventDeletedEventHandler {

    private static final String OPEN_JOB_NAME = "EVENT_OPEN_JOB-";
    private static final String CLOSE_JOB_NAME = "EVENT_CLOSE_JOB-";
    private static final String EVENT_JOB_GROUP = "EVENT_JOB_GROUP";

    private final Scheduler scheduler;

    @TransactionalEventListener(
            classes = LockerEventDeletedEvent.class,
            phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(LockerEventDeletedEvent lockerEventDeletedEvent) throws SchedulerException {
        Long eventId = lockerEventDeletedEvent.getEventId();

        // Event Open Job 삭제
        JobKey openJobKey = new JobKey(OPEN_JOB_NAME + eventId, EVENT_JOB_GROUP);
        if (scheduler.checkExists(openJobKey)) {
            scheduler.deleteJob(openJobKey);
        }

        // Event Close Job 삭제
        JobKey closeJobKey = new JobKey(CLOSE_JOB_NAME + eventId, EVENT_JOB_GROUP);
        if (scheduler.checkExists(closeJobKey)) {
            scheduler.deleteJob(closeJobKey);
        }
    }
}
