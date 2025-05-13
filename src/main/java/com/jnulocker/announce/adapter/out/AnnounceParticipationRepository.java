package com.jnulocker.announce.adapter.out;

import com.jnulocker.announce.domain.Announce;
import com.jnulocker.announce.domain.AnnounceParticipation;
import com.jnulocker.organization.domain.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceParticipationRepository
        extends JpaRepository<AnnounceParticipation, Long> {
    void deleteAllByAnnounce(Announce announce);

    @EntityGraph(attributePaths = {"announce"})
    Optional<AnnounceParticipation> findByAnnounceIdAndDepartment(
            Long announceId, Department department);

    @EntityGraph(attributePaths = {"department"})
    List<AnnounceParticipation> findAllByAnnounce(Announce announce);
}
