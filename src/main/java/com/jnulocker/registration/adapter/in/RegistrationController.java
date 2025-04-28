package com.jnulocker.registration.adapter.in;

import com.jnulocker.registration.adapter.in.docs.RegistrationApi;
import com.jnulocker.registration.application.port.in.RegistrationCommand;
import com.jnulocker.registration.application.port.in.request.RegisterForEventRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/{event-id}/registrations")
public class RegistrationController implements RegistrationApi {

    private final RegistrationCommand registrationCommand;

    @Override
    @PostMapping
    public ResponseEntity<Void> registerForEvent(
            @PathVariable("event-id") Long eventId,
            @Valid @RequestBody RegisterForEventRequest request) {
        registrationCommand.registerForEvent(eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
