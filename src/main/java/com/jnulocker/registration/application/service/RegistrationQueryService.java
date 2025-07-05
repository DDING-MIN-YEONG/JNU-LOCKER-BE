package com.jnulocker.registration.application.service;

import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.domain.Event;
import com.jnulocker.registration.application.port.in.RegistrationQuery;
import com.jnulocker.registration.application.port.in.response.RegistrationCustomPage;
import com.jnulocker.registration.application.port.in.response.RegistrationListItem;
import com.jnulocker.registration.application.port.in.response.RegistrationResponse;
import com.jnulocker.registration.application.port.out.RegistrationLoadPort;
import com.jnulocker.registration.domain.Registration;
import com.jnulocker.registration.exception.RegistrationNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationQueryService implements RegistrationQuery {

    private final EventQuery eventQuery;
    private final RegistrationLoadPort registrationLoadPort;

    @Override
    public RegistrationCustomPage getRegistrations(UUID eventId, Pageable pageable) {
        Event event = eventQuery.getByIdOrThrow(eventId);
        Page<Registration> registrations =
                registrationLoadPort.getRegistrationsByEventId(event.getId(), pageable);
        return RegistrationCustomPage.from(registrations);
    }

    @Override
    public List<RegistrationListItem> getAllRegistrations(UUID eventId) {
        Event event = eventQuery.getByIdOrThrow(eventId);
        List<Registration> registrations =
                registrationLoadPort.getAllRegistrationsByEventId(event.getId());
        return registrations.stream().map(RegistrationListItem::from).toList();
    }

    @Override
    public RegistrationResponse getMyRegistration(UUID eventId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Registration registration =
                registrationLoadPort
                        .getRegistrationByMemberIdAndEventId(memberId, eventId)
                        .orElseThrow(() -> RegistrationNotFoundException.EXCEPTION);
        return RegistrationResponse.from(registration);
    }
}
