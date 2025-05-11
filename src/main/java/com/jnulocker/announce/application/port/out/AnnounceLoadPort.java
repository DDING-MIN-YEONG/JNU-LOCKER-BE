package com.jnulocker.announce.application.port.out;

import com.jnulocker.announce.domain.Announce;
import com.jnulocker.organization.domain.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnounceLoadPort {

    Page<Announce> getAllAnnouncesByDepartment(Department department, Pageable pageable);

    List<Announce> getAnnouncesByParticipationDepartment(Department department);

    Optional<Announce> getById(Long announceId);
}
