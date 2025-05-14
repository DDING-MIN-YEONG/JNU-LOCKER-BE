package com.jnulocker.events.application.port.in;

import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.EventResponse;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.Locker;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface EventQuery {

    Event getByIdOrThrow(UUID eventId);

    MyEventCustomPage getMyEvents(Pageable pageable);

    EventCustomPage getAllEvents(Pageable pageable);

    Locker getLockerByIdOrThrow(Long lockerId);

    List<FloorWithLockersResponse> getLockersByEventId(UUID eventId);

    EventResponse getEvent(UUID eventId);
}
