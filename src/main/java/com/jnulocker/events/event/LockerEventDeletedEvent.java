package com.jnulocker.events.event;

import com.jnulocker.common.event.DomainEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LockerEventDeletedEvent extends DomainEvent {

    private final Long eventId;

    public static LockerEventDeletedEvent of(Long eventId) {
        return new LockerEventDeletedEvent(eventId);
    }
}
