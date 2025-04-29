package com.jnulocker.registration.application.port.in;

import com.jnulocker.registration.application.port.in.response.RegistrationCustomPage;
import com.jnulocker.registration.application.port.in.response.RegistrationResponse;
import org.springframework.data.domain.Pageable;

public interface RegistrationQuery {
    RegistrationCustomPage getRegistrations(Long eventId, Pageable pageable);

    RegistrationResponse getMyRegistration(Long eventId);
}
