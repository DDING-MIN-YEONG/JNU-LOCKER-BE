package com.jnulocker.registration.application.port.in;

import com.jnulocker.registration.application.port.in.response.RegistrationCustomPage;
import com.jnulocker.registration.application.port.in.response.RegistrationResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface RegistrationQuery {
    RegistrationCustomPage getRegistrations(UUID eventId, Pageable pageable);

    RegistrationResponse getMyRegistration(UUID eventId);
}
