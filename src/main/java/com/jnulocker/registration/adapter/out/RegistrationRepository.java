package com.jnulocker.registration.adapter.out;

import com.jnulocker.registration.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByMemberIdAndLocker_Floor_EventId(Long memberId, Long eventId);
}
