package com.jnulocker.events.adapter.in;

import com.jnulocker.events.adapter.in.docs.EventApi;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class EventController implements EventApi {

    private final EventQuery eventQuery;

    @Override
    @GetMapping("/{event-id}/lockers")
    public ResponseEntity<List<FloorWithLockersResponse>> getLockers(
            @PathVariable("event-id") Long eventId) {
        return ResponseEntity.ok(eventQuery.getLockersByEventId(eventId));
    }
}
