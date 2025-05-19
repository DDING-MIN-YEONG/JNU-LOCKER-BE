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
public class LockerEventUpdatedEventHandler {

    private final QuartzSchedulerUtil quartzSchedulerUtil;

    @TransactionalEventListener(
            classes = LockerEventUpdatedEvent.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void handle(LockerEventUpdatedEvent event) throws SchedulerException {
        UUID eventId = event.getEventId();

        // 기존 스케줄링 작업 삭제
        quartzSchedulerUtil.deleteEventJobs(eventId);

        // 새로운 스케줄링 작업 생성
        quartzSchedulerUtil.scheduleEventJobs(eventId, event.getStartAt(), event.getEndAt());
    }
}
