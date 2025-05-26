package com.jnulocker.events.application.port.out;

import com.jnulocker.events.domain.Locker;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LockerLoadPort {

    Optional<Locker> getById(UUID lockerId);

    List<Locker> getLockersByFloorId(Long floorId);
}
