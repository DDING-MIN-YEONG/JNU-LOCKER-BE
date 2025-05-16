package com.jnulocker.events.quartz;

import static com.jnulocker.events.quartz.EventSchedulerConstants.ASIA_SEOUL;
import static com.jnulocker.events.quartz.EventSchedulerConstants.CLOSE_JOB_NAME;
import static com.jnulocker.events.quartz.EventSchedulerConstants.CLOSE_TRIGGER_NAME;
import static com.jnulocker.events.quartz.EventSchedulerConstants.EVENT_ID;
import static com.jnulocker.events.quartz.EventSchedulerConstants.EVENT_JOB_GROUP;
import static com.jnulocker.events.quartz.EventSchedulerConstants.EVENT_TRIGGER_GROUP;
import static com.jnulocker.events.quartz.EventSchedulerConstants.ONE_DAY;
import static com.jnulocker.events.quartz.EventSchedulerConstants.OPEN_JOB_NAME;
import static com.jnulocker.events.quartz.EventSchedulerConstants.OPEN_TRIGGER_NAME;
import static com.jnulocker.events.quartz.EventSchedulerConstants.PUBLISH_JOB_NAME;
import static com.jnulocker.events.quartz.EventSchedulerConstants.PUBLISH_TRIGGER_NAME;
import static com.jnulocker.events.quartz.EventSchedulerConstants.TWO_HOURS;
import static com.jnulocker.events.quartz.EventSchedulerConstants.UNPUBLISH_JOB_NAME;
import static com.jnulocker.events.quartz.EventSchedulerConstants.UNPUBLISH_TRIGGER_NAME;
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
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuartzSchedulerUtil {

    private final Scheduler scheduler;

    public void deleteEventJobs(UUID eventId) throws SchedulerException {
        deleteJob(PUBLISH_JOB_NAME, eventId);
        deleteJob(OPEN_JOB_NAME, eventId);
        deleteJob(CLOSE_JOB_NAME, eventId);
        deleteJob(UNPUBLISH_JOB_NAME, eventId);
    }

    private void deleteJob(String jobName, UUID eventId) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName + eventId, EVENT_JOB_GROUP);
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
    }

    public void scheduleEventJobs(UUID eventId, LocalDateTime startAt, LocalDateTime endAt) {
        scheduleEventJob( // Event Publish Job 등록: OPEN 2시간 전에 publish를 true로 변경
                startAt.minusHours(TWO_HOURS),
                newJob(EventPublishJob.class),
                PUBLISH_JOB_NAME,
                eventId,
                PUBLISH_TRIGGER_NAME);

        scheduleEventJob( // Event Open Job 등록
                startAt, newJob(EventOpenJob.class), OPEN_JOB_NAME, eventId, OPEN_TRIGGER_NAME);

        scheduleEventJob( // Event Close Job 등록
                endAt, newJob(EventCloseJob.class), CLOSE_JOB_NAME, eventId, CLOSE_TRIGGER_NAME);

        scheduleEventJob( // Event Unpublish Job 등록: CLOSE 1일 후에 publish를 false로 변경
                endAt.plusDays(ONE_DAY),
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
