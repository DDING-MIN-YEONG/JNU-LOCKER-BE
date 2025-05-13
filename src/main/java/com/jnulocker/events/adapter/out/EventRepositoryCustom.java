package com.jnulocker.events.adapter.out;

import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import com.jnulocker.organization.domain.Department;
import org.springframework.data.domain.Pageable;

public interface EventRepositoryCustom {
    MyEventCustomPage findEventsByParticipationDepartment(Department department, Pageable pageable);
}
