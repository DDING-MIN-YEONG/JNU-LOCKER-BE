package com.jnulocker.registration.adapter.out;

import com.jnulocker.events.domain.Event;
import com.jnulocker.member.domain.Member;
import com.jnulocker.registration.domain.Registration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByMemberIdAndLocker_Floor_EventId(Long memberId, UUID eventId);

    @EntityGraph(
            attributePaths = {
                "member",
                "member.department",
                "member.department.organization",
                "locker",
                "locker.floor"
            })
    Page<Registration> findAllByLocker_Floor_EventId(UUID eventId, Pageable pageable);

    @EntityGraph(attributePaths = {"member", "member.department", "locker", "locker.floor"})
    Optional<Registration> findByMemberIdAndLocker_Floor_EventId(Long memberId, UUID eventId);

    void deleteAllByLocker_Floor_Event(Event event);

    @EntityGraph(attributePaths = {"locker"})
    List<Registration> findAllByMember(Member member);

    void deleteAllByMember(Member member);
}
