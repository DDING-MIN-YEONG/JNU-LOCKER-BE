package com.jnulocker.events.application.service;

import com.jnulocker.events.application.port.in.GetEventLockerQuery;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.LockerResponse;
import com.jnulocker.events.application.port.out.LoadEventPort;
import com.jnulocker.events.application.port.out.LoadFloorPort;
import com.jnulocker.events.application.port.out.LoadLockerPort;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.exception.EventNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetEventLockerService implements GetEventLockerQuery {

    private final LoadEventPort loadEventPort;
    private final LoadFloorPort loadFloorPort;
    private final LoadLockerPort loadLockerPort;

    @Override
    public List<FloorWithLockersResponse> getLockersByEventId(Long eventId) {
        if (!loadEventPort.existsById(eventId)) {
            throw EventNotFoundException.EXCEPTION;
        }

        List<Floor> floors = loadFloorPort.getFloorsByEventId(eventId);

        return floors.stream()
                .map(
                        floor ->
                                FloorWithLockersResponse.of(
                                        floor.getId(),
                                        floor.getFloorNumber(),
                                        getLockerResponsesByFloor(floor)))
                .toList();
    }

    private List<LockerResponse> getLockerResponsesByFloor(Floor floor) {
        return loadLockerPort.getLockersByFloorId(floor.getId()).stream()
                .map(LockerResponse::from)
                .toList();
    }
}
