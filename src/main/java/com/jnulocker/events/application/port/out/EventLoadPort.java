package com.jnulocker.events.application.port.out;

import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import com.jnulocker.events.domain.Event;
import com.jnulocker.organization.domain.Department;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventLoadPort {

    boolean existsById(UUID eventId);

    Optional<Event> getById(UUID eventId);

    Page<Event> getAllEventsByDepartment(Department department, Pageable pageable);

    MyEventCustomPage getEventsByParticipationDepartment(Department department, Pageable pageable);

    Optional<Event> getEventByIdWithEventParticipation(UUID eventId);
}
