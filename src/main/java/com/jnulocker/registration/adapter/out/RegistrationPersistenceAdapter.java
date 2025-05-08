package com.jnulocker.registration.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.registration.application.port.out.RegistrationLoadPort;
import com.jnulocker.registration.application.port.out.RegistrationRecordPort;
import com.jnulocker.registration.domain.Registration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@PersistenceAdapter
@RequiredArgsConstructor
public class RegistrationPersistenceAdapter
        implements RegistrationLoadPort, RegistrationRecordPort {

    private final RegistrationRepository registrationRepository;

    @Override
    public void save(Registration registration) {
        registrationRepository.save(registration);
    }

    @Override
    public void delete(Registration registration) {
        registrationRepository.delete(registration);
    }

    @Override
    public boolean existsByMemberIdAndEventId(Long memberId, UUID eventId) {
        return registrationRepository.existsByMemberIdAndLocker_Floor_EventId(memberId, eventId);
    }

    @Override
    public Page<Registration> getRegistrationsByEventId(UUID eventId, Pageable pageable) {
        return registrationRepository.findAllByLocker_Floor_EventId(eventId, pageable);
    }

    @Override
    public Optional<Registration> getRegistrationByMemberIdAndEventId(Long memberId, UUID eventId) {
        return registrationRepository.findByMemberIdAndLocker_Floor_EventId(memberId, eventId);
    }
}
