package com.jnulocker.events.quartz.job;

import com.jnulocker.events.application.port.in.EventCommand;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.domain.Event;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@RequiredArgsConstructor
public class EventPublishJob implements Job {

    private final EventQuery eventQuery;
    private final EventCommand eventCommand;

    @Setter private Long eventId;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Event event = eventQuery.getByIdOrThrow(eventId);
        event.updatePublishStatus(Boolean.TRUE);
        eventCommand.save(event);
    }
}
