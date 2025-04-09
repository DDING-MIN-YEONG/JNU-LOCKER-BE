package com.jnulocker.events.application.port.in;

import com.jnulocker.events.application.port.in.request.CreateEventRequest;
import com.jnulocker.events.domain.Event;

public interface EventCommand {
    void createEvent(CreateEventRequest request);

    void save(Event event);
}
