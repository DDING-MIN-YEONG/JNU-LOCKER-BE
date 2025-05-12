package com.jnulocker.announce.adapter.out;

import com.jnulocker.announce.domain.Announce;
import com.jnulocker.organization.domain.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceRepository extends JpaRepository<Announce, Long> {

    Page<Announce> findAllByDepartment(Department department, Pageable pageable);

    @EntityGraph(attributePaths = {"department"})
    List<Announce> findAllByAnnounceParticipations_Department(Department department);

    Optional<Announce> findByIdAndDepartment(Long announceId, Department department);
}
