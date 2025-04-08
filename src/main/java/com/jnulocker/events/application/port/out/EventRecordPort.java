package com.jnulocker.events.application.port.out;

import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventParticipation;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import java.util.List;

public interface EventRecordPort {
    Event saveEvent(Event event);

    void saveEventParticipations(List<EventParticipation> eventParticipations);

    Floor saveFloor(Floor floor);

    void saveLockers(List<Locker> lockers);
}
