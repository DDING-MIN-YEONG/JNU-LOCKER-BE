package com.jnulocker.events.application.port.out;

import com.jnulocker.events.domain.Floor;
import java.util.List;
import java.util.UUID;

public interface FloorLoadPort {

    List<Floor> getFloorsByEventId(UUID eventId);
}
