package com.jnulocker.events.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.events.application.port.out.FloorLoadPort;
import com.jnulocker.events.domain.Floor;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FloorPersistenceAdapter implements FloorLoadPort {

    private final FloorRepository floorRepository;

    @Override
    public List<Floor> getFloorsByEventId(Long eventId) {
        return floorRepository.findAllByEventId(eventId);
    }
}
