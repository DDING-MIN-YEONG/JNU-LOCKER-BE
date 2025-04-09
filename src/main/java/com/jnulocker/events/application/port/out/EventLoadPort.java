package com.jnulocker.events.application.port.out;

import com.jnulocker.events.domain.Event;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventLoadPort {

    boolean existsById(Long eventId);

    Optional<Event> getById(Long eventId);

    Page<Event> getAllEvents(Pageable pageable);
}
