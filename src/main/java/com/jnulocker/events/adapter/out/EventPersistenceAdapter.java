package com.jnulocker.events.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.events.application.port.out.LoadEventPort;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class EventPersistenceAdapter implements LoadEventPort {

    private final EventRepository eventRepository;

    @Override
    public boolean existsById(Long eventId) {
        return eventRepository.existsById(eventId);
    }
}
