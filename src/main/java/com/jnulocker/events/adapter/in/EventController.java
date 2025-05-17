package com.jnulocker.events.adapter.in;

import com.jnulocker.events.adapter.in.docs.EventApi;
import com.jnulocker.events.application.port.in.EventCommand;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.request.CreateEventRequest;
import com.jnulocker.events.application.port.in.request.PublishEventRequest;
import com.jnulocker.events.application.port.in.request.UpdateEventRequest;
import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.EventPageable;
import com.jnulocker.events.application.port.in.response.EventResponse;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class EventController implements EventApi {

    private final EventQuery eventQuery;
    private final EventCommand eventCommand;

    @Override
    @GetMapping
    public ResponseEntity<EventCustomPage> getEvents(
            @Valid @ParameterObject EventPageable eventPageable) {
        Pageable pageable = eventPageable.toPageable();
        return ResponseEntity.ok(eventQuery.getAllEvents(pageable));
    }

    @Override
    @GetMapping("/{event-id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable("event-id") UUID eventId) {
        return ResponseEntity.ok(eventQuery.getEvent(eventId));
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<MyEventCustomPage> getMyEvents(
            @Valid @ParameterObject EventPageable eventPageable) {
        Pageable pageable = eventPageable.toPageable();
        return ResponseEntity.ok(eventQuery.getMyEvents(pageable));
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> createEvent(@Valid @RequestBody CreateEventRequest request) {
        eventCommand.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PutMapping("/{event-id}")
    public ResponseEntity<Void> updateEvent(
            @PathVariable("event-id") UUID eventId,
            @Valid @RequestBody UpdateEventRequest request) {
        eventCommand.updateEvent(eventId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @GetMapping("/{event-id}/lockers")
    public ResponseEntity<List<FloorWithLockersResponse>> getLockers(
            @PathVariable("event-id") UUID eventId) {
        return ResponseEntity.ok(eventQuery.getLockersByEventId(eventId));
    }

    @Override
    @DeleteMapping("/{event-id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("event-id") UUID eventId) {
        eventCommand.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PutMapping("/{event-id}/publish")
    public ResponseEntity<Void> publishEvent(
            @PathVariable("event-id") UUID eventId,
            @RequestBody @Valid PublishEventRequest request) {
        eventCommand.publishEvent(eventId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
