package com.jnulocker.registration.application.port.in;

import com.jnulocker.registration.application.port.in.request.RegisterForEventRequest;

public interface RegistrationCommand {
    void registerForEvent(Long eventId, RegisterForEventRequest request);

    void cancelMyRegistration(Long eventId);
}
