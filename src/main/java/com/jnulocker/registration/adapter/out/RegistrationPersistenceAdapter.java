package com.jnulocker.registration.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.registration.application.port.out.RegistrationRecordPort;
import com.jnulocker.registration.domain.Registration;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class RegistrationPersistenceAdapter implements RegistrationRecordPort {

    private final RegistrationRepository registrationRepository;

    @Override
    public void save(Registration registration) {
        registrationRepository.save(registration);
    }
}
