package com.jnulocker.events.application.port.out;

import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.Locker;
import java.util.List;
import java.util.Optional;

public interface LockerLoadPort {

    Optional<Locker> getById(Long lockerId);

    List<Locker> getLockersByFloorId(Long floorId);

    Integer getAvailableLockerCountOfEvent(Event event);
}
