package com.jnulocker.registration.adapter.out;

import com.jnulocker.registration.domain.Registration;
import java.util.List;
import java.util.UUID;

public interface RegistrationRepositoryCustom {

    List<Registration> findAllByEventId(UUID eventId);
}
