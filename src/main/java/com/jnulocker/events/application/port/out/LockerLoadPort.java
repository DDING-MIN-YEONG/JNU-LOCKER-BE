package com.jnulocker.events.application.port.out;

import com.jnulocker.events.domain.Locker;
import java.util.List;

public interface LockerLoadPort {

    List<Locker> getLockersByFloorId(Long floorId);
}
