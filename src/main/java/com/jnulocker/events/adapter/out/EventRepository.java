package com.jnulocker.events.adapter.out;

import com.jnulocker.events.domain.Event;
import com.jnulocker.organization.domain.Department;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, UUID>, EventRepositoryCustom {

    Page<Event> findAllByDepartment(Department department, Pageable pageable);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventParticipations WHERE e.id = :eventId")
    Optional<Event> findByIdWithEventParticipations(@Param("eventId") UUID eventId);
}
