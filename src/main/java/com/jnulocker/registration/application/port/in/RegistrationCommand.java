package com.jnulocker.registration.application.port.in;

import com.jnulocker.registration.application.port.in.request.RegisterForEventRequest;
import java.util.UUID;

public interface RegistrationCommand {
    void registerForEvent(UUID eventId, RegisterForEventRequest request);

    void cancelMyRegistration(UUID eventId);
}
