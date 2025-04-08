package com.jnulocker.events.application.port.out;

import com.jnulocker.events.domain.Floor;
import java.util.List;

public interface FloorLoadPort {

    List<Floor> getFloorsByEventId(Long eventId);
}
