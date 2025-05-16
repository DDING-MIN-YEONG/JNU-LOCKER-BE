package com.jnulocker.events.event;

import com.jnulocker.common.event.DomainEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LockerEventUpdatedEvent extends DomainEvent {

    private final UUID eventId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public static LockerEventUpdatedEvent of(
            UUID eventId, LocalDateTime startAt, LocalDateTime endAt) {
        return new LockerEventUpdatedEvent(eventId, startAt, endAt);
    }
}
