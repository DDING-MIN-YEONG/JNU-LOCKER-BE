package com.jnulocker.events.application.port.in;

import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.domain.Event;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface EventQuery {

    Event getByIdOrThrow(Long eventId);

    EventCustomPage getAllEvents(Pageable pageable);

    List<FloorWithLockersResponse> getLockersByEventId(Long eventId);
}
