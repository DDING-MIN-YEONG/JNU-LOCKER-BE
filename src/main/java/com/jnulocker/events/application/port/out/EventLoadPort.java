package com.jnulocker.events.application.port.out;

import com.jnulocker.events.domain.Event;
import java.util.Optional;

public interface EventLoadPort {

    boolean existsById(Long eventId);

    Optional<Event> getById(Long eventId);
}
