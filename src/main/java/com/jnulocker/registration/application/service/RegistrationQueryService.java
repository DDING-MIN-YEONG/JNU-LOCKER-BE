package com.jnulocker.registration.application.service;

import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.domain.Event;
import com.jnulocker.registration.application.port.in.RegistrationQuery;
import com.jnulocker.registration.application.port.in.response.RegistrationCustomPage;
import com.jnulocker.registration.application.port.in.response.RegistrationResponse;
import com.jnulocker.registration.application.port.out.RegistrationLoadPort;
import com.jnulocker.registration.domain.Registration;
import com.jnulocker.registration.exception.RegistrationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationQueryService implements RegistrationQuery {

    private final EventQuery eventQuery;
    private final RegistrationLoadPort registrationLoadPort;

    @Override
    public RegistrationCustomPage getRegistrations(Long eventId, Pageable pageable) {
        Event event = eventQuery.getByIdOrThrow(eventId);
        Page<Registration> registrations =
                registrationLoadPort.getRegistrationsByEventId(event.getId(), pageable);
        return RegistrationCustomPage.from(registrations);
    }

    @Override
    public RegistrationResponse getMyRegistration(Long eventId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Registration registration =
                registrationLoadPort
                        .getRegistrationByMemberIdAndEventId(memberId, eventId)
                        .orElseThrow(() -> RegistrationNotFoundException.EXCEPTION);
        return RegistrationResponse.from(registration);
    }
}
