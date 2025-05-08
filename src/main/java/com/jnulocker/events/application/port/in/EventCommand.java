package com.jnulocker.events.application.port.in;

import com.jnulocker.events.application.port.in.request.CreateEventRequest;
import com.jnulocker.events.application.port.in.request.PublishEventRequest;
import com.jnulocker.events.domain.Event;
import java.util.UUID;

public interface EventCommand {
    void createEvent(CreateEventRequest request);

    void deleteEvent(UUID eventId);

    void save(Event event);

    void publishEvent(UUID eventId, PublishEventRequest request);
}
