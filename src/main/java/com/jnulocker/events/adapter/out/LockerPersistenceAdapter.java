package com.jnulocker.events.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.events.application.port.out.LoadLockerPort;
import com.jnulocker.events.domain.Locker;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class LockerPersistenceAdapter implements LoadLockerPort {

    private final LockerRepository lockerRepository;

    @Override
    public List<Locker> getLockersByFloorId(Long floorId) {
        return lockerRepository.findAllByFloorId(floorId);
    }
}
