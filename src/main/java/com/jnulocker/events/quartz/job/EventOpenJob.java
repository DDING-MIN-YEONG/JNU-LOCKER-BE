package com.jnulocker.events.quartz.job;

import com.jnulocker.events.application.port.in.EventCommand;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.domain.Event;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@RequiredArgsConstructor
public class EventOpenJob implements Job {

    private final EventQuery eventQuery;
    private final EventCommand eventCommand;

    @Setter private Long eventId; // JobDataMap에서 eventId를 가져오기 위해 Setter 사용

    @Override
    public void execute(JobExecutionContext context) {
        Event event = eventQuery.getByIdOrThrow(eventId);
        event.openEvent();
        eventCommand.save(event);
    }
}
