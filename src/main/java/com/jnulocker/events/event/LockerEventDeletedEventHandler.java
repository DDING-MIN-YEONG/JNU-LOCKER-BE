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

    private static final String PUBLISH_JOB_NAME = "EVENT_PUBLISH_JOB-";
    private static final String OPEN_JOB_NAME = "EVENT_OPEN_JOB-";
    private static final String CLOSE_JOB_NAME = "EVENT_CLOSE_JOB-";
    private static final String UNPUBLISH_JOB_NAME = "EVENT_UNPUBLISH_JOB-";
    private static final String EVENT_JOB_GROUP = "EVENT_JOB_GROUP";

    private final Scheduler scheduler;

    @TransactionalEventListener(
            classes = LockerEventDeletedEvent.class,
            phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(LockerEventDeletedEvent lockerEventDeletedEvent) throws SchedulerException {
        Long eventId = lockerEventDeletedEvent.getEventId();

        // Event Publish Job 삭제
        deleteJob(PUBLISH_JOB_NAME, eventId);

        // Event Open Job 삭제
        deleteJob(OPEN_JOB_NAME, eventId);

        // Event Close Job 삭제
        deleteJob(CLOSE_JOB_NAME, eventId);

        // Event Unpublish Job 삭제
        deleteJob(UNPUBLISH_JOB_NAME, eventId);
    }

    private void deleteJob(String jobName, Long eventId) throws SchedulerException {
        JobKey publishJobKey = new JobKey(jobName + eventId, EVENT_JOB_GROUP);
        if (scheduler.checkExists(publishJobKey)) {
            scheduler.deleteJob(publishJobKey);
        }
    }
}
