package com.jnulocker.events.event;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.jnulocker.events.quartz.job.EventCloseJob;
import com.jnulocker.events.quartz.job.EventOpenJob;
import com.jnulocker.events.quartz.job.EventPublishJob;
import com.jnulocker.events.quartz.job.EventUnpublishJob;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class LockerEventCreatedEventHandler {

    private static final String PUBLISH_JOB_NAME = "EVENT_PUBLISH_JOB-";
    private static final String PUBLISH_TRIGGER_NAME = "EVENT_PUBLISH_TRIGGER-";

    private static final String UNPUBLISH_JOB_NAME = "EVENT_UNPUBLISH_JOB-";
    private static final String UNPUBLISH_TRIGGER_NAME = "EVENT_UNPUBLISH_TRIGGER-";

    private static final String OPEN_JOB_NAME = "EVENT_OPEN_JOB-";
    private static final String OPEN_TRIGGER_NAME = "EVENT_OPEN_TRIGGER-";

    private static final String CLOSE_JOB_NAME = "EVENT_CLOSE_JOB-";
    private static final String CLOSE_TRIGGER_NAME = "EVENT_CLOSE_TRIGGER-";

    private static final String EVENT_JOB_GROUP = "EVENT_JOB_GROUP";
    private static final String EVENT_TRIGGER_GROUP = "EVENT_TRIGGER_GROUP";

    private static final String EVENT_ID = "eventId";
    private static final String ASIA_SEOUL = "Asia/Seoul";

    private static final Long TWO_HOURS = 2L;
    private static final Long ONE_DAY = 1L;

    private final Scheduler scheduler;

    @TransactionalEventListener(
            classes = LockerEventCreatedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void handle(LockerEventCreatedEvent lockerEventCreatedEvent) {

        UUID eventId = lockerEventCreatedEvent.getEventId();

        scheduleEventJob( // Event Publish Job 등록: OPEN 2시간 전에 publish를 true로 변경
                lockerEventCreatedEvent.getStartAt().minusHours(TWO_HOURS),
                newJob(EventPublishJob.class),
                PUBLISH_JOB_NAME,
                eventId,
                PUBLISH_TRIGGER_NAME);

        scheduleEventJob( // Event Open Job 등록
                lockerEventCreatedEvent.getStartAt(),
                newJob(EventOpenJob.class),
                OPEN_JOB_NAME,
                eventId,
                OPEN_TRIGGER_NAME);

        scheduleEventJob( // Event Close Job 등록
                lockerEventCreatedEvent.getEndAt(),
                newJob(EventCloseJob.class),
                CLOSE_JOB_NAME,
                eventId,
                CLOSE_TRIGGER_NAME);

        scheduleEventJob( // Event Unpublish Job 등록: CLOSE 1일 후에 publish를 false로 변경
                lockerEventCreatedEvent.getEndAt().plusDays(ONE_DAY),
                newJob(EventUnpublishJob.class),
                UNPUBLISH_JOB_NAME,
                eventId,
                UNPUBLISH_TRIGGER_NAME);
    }

    private void scheduleEventJob(
            LocalDateTime localDateTime,
            JobBuilder newJob,
            String jobName,
            UUID eventId,
            String triggerName) {
        Date startAt = Date.from(localDateTime.atZone(ZoneId.of(ASIA_SEOUL)).toInstant());

        JobDetail jobDetail =
                newJob.withIdentity(jobName + eventId, EVENT_JOB_GROUP)
                        .usingJobData(EVENT_ID, eventId.toString())
                        .build();

        Trigger eventOpenTrigger =
                newTrigger()
                        .withIdentity(triggerName + eventId, EVENT_TRIGGER_GROUP)
                        .forJob(jobDetail)
                        .startAt(startAt)
                        .build();

        try {
            scheduler.scheduleJob(jobDetail, eventOpenTrigger);
        } catch (SchedulerException e) {
            log.error("스케줄링 작업 생성 실패: jobName={}, eventId={}", jobName, eventId, e);
        }
    }
}
