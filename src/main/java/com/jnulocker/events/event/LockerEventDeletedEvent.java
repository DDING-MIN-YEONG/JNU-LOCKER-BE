package com.jnulocker.events.event;

import com.jnulocker.common.event.DomainEvent;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LockerEventDeletedEvent extends DomainEvent {

    private final UUID eventId;

    public static LockerEventDeletedEvent of(UUID eventId) {
        return new LockerEventDeletedEvent(eventId);
    }
}
