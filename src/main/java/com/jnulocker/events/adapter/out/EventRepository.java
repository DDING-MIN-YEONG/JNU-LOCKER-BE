package com.jnulocker.events.adapter.out;

import com.jnulocker.events.domain.Event;
import com.jnulocker.organization.domain.Department;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, UUID>, EventRepositoryCustom {

    Page<Event> findAllByDepartment(Department department, Pageable pageable);
}
