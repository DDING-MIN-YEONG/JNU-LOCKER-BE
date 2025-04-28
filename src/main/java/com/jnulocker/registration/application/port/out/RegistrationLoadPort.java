package com.jnulocker.registration.application.port.out;

public interface RegistrationLoadPort {

    boolean existsByMemberIdAndEventId(Long memberId, Long eventId);
}
