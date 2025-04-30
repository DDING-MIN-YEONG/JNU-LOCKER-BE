package com.jnulocker.registration.adapter.in;

import com.jnulocker.registration.adapter.in.docs.RegistrationApi;
import com.jnulocker.registration.application.port.in.RegistrationCommand;
import com.jnulocker.registration.application.port.in.RegistrationQuery;
import com.jnulocker.registration.application.port.in.request.RegisterForEventRequest;
import com.jnulocker.registration.application.port.in.response.RegistrationCustomPage;
import com.jnulocker.registration.application.port.in.response.RegistrationPageable;
import com.jnulocker.registration.application.port.in.response.RegistrationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events/{event-id}/registrations")
public class RegistrationController implements RegistrationApi {

    private final RegistrationQuery registrationQuery;
    private final RegistrationCommand registrationCommand;

    @Override
    @GetMapping
    public ResponseEntity<RegistrationCustomPage> getRegistrations(
            @PathVariable("event-id") Long eventId,
            @Valid @ParameterObject RegistrationPageable registrationPageable) {
        Pageable pageable = registrationPageable.toPageable();
        return ResponseEntity.ok(registrationQuery.getRegistrations(eventId, pageable));
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<RegistrationResponse> getMyRegistration(
            @PathVariable("event-id") Long eventId) {
        return ResponseEntity.ok(registrationQuery.getMyRegistration(eventId));
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> registerForEvent(
            @PathVariable("event-id") Long eventId,
            @Valid @RequestBody RegisterForEventRequest request) {
        registrationCommand.registerForEvent(eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @DeleteMapping("/me")
    public ResponseEntity<Void> cancelMyRegistration(@PathVariable("event-id") Long eventId) {
        registrationCommand.cancelMyRegistration(eventId);
        return ResponseEntity.noContent().build();
    }
}
