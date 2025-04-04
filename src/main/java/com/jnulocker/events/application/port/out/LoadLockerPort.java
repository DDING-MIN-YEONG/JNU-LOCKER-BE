package com.jnulocker.events.application.port.out;

import com.jnulocker.events.domain.Locker;
import java.util.List;

public interface LoadLockerPort {

    List<Locker> getLockersByFloorId(Long floorId);
}
