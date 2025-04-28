package com.jnulocker.registration.application.port.out;

import com.jnulocker.registration.domain.Registration;

public interface RegistrationRecordPort {

    void save(Registration registration);
}
