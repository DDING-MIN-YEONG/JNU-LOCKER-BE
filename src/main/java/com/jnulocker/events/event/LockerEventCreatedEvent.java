package com.jnulocker.events.event;

import com.jnulocker.common.event.DomainEvent;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LockerEventCreatedEvent extends DomainEvent {

    private final Long eventId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public static LockerEventCreatedEvent of(
            Long eventId, LocalDateTime startAt, LocalDateTime endAt) {
        return new LockerEventCreatedEvent(eventId, startAt, endAt);
    }
}
