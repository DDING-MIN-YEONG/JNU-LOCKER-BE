package com.jnulocker.registration.application.port.out;

import com.jnulocker.registration.domain.Registration;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RegistrationLoadPort {

    boolean existsByMemberIdAndEventId(Long memberId, Long eventId);

    Page<Registration> getRegistrationsByEventId(Long eventId, Pageable pageable);

    Optional<Registration> getRegistrationByMemberIdAndEventId(Long memberId, Long eventId);
}
