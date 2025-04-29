package com.jnulocker.registration.adapter.out;

import com.jnulocker.registration.domain.Registration;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByMemberIdAndLocker_Floor_EventId(Long memberId, Long eventId);

    @Query(
            "SELECT r FROM Registration r JOIN FETCH r.member m JOIN FETCH m.department JOIN FETCH r.locker l JOIN FETCH l.floor f WHERE f.event.id = :eventId")
    Page<Registration> findAllByEventId(Long eventId, Pageable pageable);

    @Query(
            "SELECT r FROM Registration r JOIN FETCH r.member m JOIN FETCH m.department d JOIN FETCH r.locker l WHERE m.id = :memberId AND l.floor.event.id = :eventId")
    Optional<Registration> findByMemberAndEvent(Long memberId, Long eventId);
}
