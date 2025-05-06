package com.jnulocker.events.adapter.out;

import com.jnulocker.events.domain.Event;
import com.jnulocker.organization.domain.Department;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(attributePaths = {"department"})
    List<Event> findAllByEventParticipations_Department(Department department);
}
