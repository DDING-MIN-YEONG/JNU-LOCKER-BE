package com.jnulocker.announce.application.port.out;

import com.jnulocker.announce.domain.Announce;
import com.jnulocker.announce.domain.AnnounceParticipation;
import com.jnulocker.organization.domain.Department;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnounceLoadPort {

    Page<Announce> getAllAnnouncesByDepartment(Department department, Pageable pageable);

    Page<Announce> getAnnouncesByParticipationDepartment(Department department, Pageable pageable);

    Optional<Announce> getById(Long announceId);

    Optional<Announce> getByIdAndDepartment(Long announceId, Department department);

    Optional<AnnounceParticipation> getByAnnounceIdAndDepartment(
            Long announceId, Department department);
}
