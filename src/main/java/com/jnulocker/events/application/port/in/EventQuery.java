package com.jnulocker.events.application.port.in;

import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.domain.Event;
import java.util.List;

public interface EventQuery {

    List<FloorWithLockersResponse> getLockersByEventId(Long eventId);

    Event getByIdOrThrow(Long eventId);
}
