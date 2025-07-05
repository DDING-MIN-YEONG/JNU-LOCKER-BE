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
import org.springframework.data.jpa.repository.Query;

public interface RegistrationRepository
        extends JpaRepository<Registration, Long>, RegistrationRepositoryCustom {
    boolean existsByMemberIdAndLocker_Floor_EventId(Long memberId, UUID eventId);

    @Query(
            """
                    SELECT r FROM Registration r
                    JOIN FETCH r.member m
                    JOIN FETCH m.department d
                    JOIN FETCH r.locker l
                    JOIN FETCH l.floor f
                    WHERE f.event.id = :eventId
                    """)
    Page<Registration> findAllByEventId(UUID eventId, Pageable pageable);

    @Query(
            """
                    SELECT r FROM Registration r
                    JOIN FETCH r.locker l
                    JOIN FETCH l.floor f
                    WHERE r.member.id = :memberId AND f.event.id = :eventId
                    """)
    Optional<Registration> findByMemberIdAndEventId(Long memberId, UUID eventId);

    void deleteAllByLocker_Floor_Event(Event event);

    @EntityGraph(attributePaths = {"locker"})
    List<Registration> findAllByMember(Member member);

    void deleteAllByMember(Member member);
}
