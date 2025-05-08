package com.jnulocker.events.quartz.job;

import com.jnulocker.events.application.port.in.EventCommand;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.domain.Event;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@RequiredArgsConstructor
public class EventUnpublishJob implements Job {

    private final EventQuery eventQuery;
    private final EventCommand eventCommand;

    @Setter private UUID eventId;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Event event = eventQuery.getByIdOrThrow(eventId);
        event.updatePublishStatus(Boolean.FALSE);
        eventCommand.save(event);
    }
}
