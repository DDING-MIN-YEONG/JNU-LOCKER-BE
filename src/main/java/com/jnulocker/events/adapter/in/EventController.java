package com.jnulocker.events.adapter.in;

import com.jnulocker.events.adapter.in.docs.EventApi;
import com.jnulocker.events.application.port.in.EventCommand;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.request.CreateEventRequest;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping
    public ResponseEntity<Void> createEvent(@Valid @RequestBody CreateEventRequest request) {
        eventCommand.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping("/{event-id}/lockers")
    public ResponseEntity<List<FloorWithLockersResponse>> getLockers(
            @PathVariable("event-id") Long eventId) {
        return ResponseEntity.ok(eventQuery.getLockersByEventId(eventId));
    }
}
