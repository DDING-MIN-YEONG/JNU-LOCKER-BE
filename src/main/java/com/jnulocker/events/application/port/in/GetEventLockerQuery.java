package com.jnulocker.events.application.port.in;

import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import java.util.List;

public interface GetEventLockerQuery {

    List<FloorWithLockersResponse> getLockersByEventId(Long eventId);
}
