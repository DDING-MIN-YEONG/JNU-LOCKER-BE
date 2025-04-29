package com.jnulocker.registration.adapter.out;

import com.jnulocker.registration.domain.Registration;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByMemberIdAndLocker_Floor_EventId(Long memberId, Long eventId);

    @EntityGraph(
            attributePaths = {
                "member",
                "member.department",
                "member.department.organization",
                "locker",
                "locker.floor"
            })
    Page<Registration> findAllByLocker_Floor_EventId(Long eventId, Pageable pageable);

    @EntityGraph(attributePaths = {"member", "member.department", "locker", "locker.floor"})
    Optional<Registration> findByMemberIdAndLocker_Floor_EventId(Long memberId, Long eventId);
}
