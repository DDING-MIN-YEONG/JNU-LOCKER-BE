package com.jnulocker.events.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.events.application.port.out.LockerLoadPort;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class LockerPersistenceAdapter implements LockerLoadPort {

    private final LockerRepository lockerRepository;

    @Override
    public Optional<Locker> getById(UUID lockerId) {
        return lockerRepository.findByIdWithFloorAndEvent(lockerId);
    }

    @Override
    public List<Locker> getLockersByFloorIds(List<Floor> floors) {
        return lockerRepository.findAllByFloorInOrderByCode(floors);
    }
}
