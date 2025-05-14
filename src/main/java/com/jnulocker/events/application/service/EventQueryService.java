package com.jnulocker.events.application.service;

import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.EventResponse;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.LockerResponse;
import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import com.jnulocker.events.application.port.out.EventLoadPort;
import com.jnulocker.events.application.port.out.FloorLoadPort;
import com.jnulocker.events.application.port.out.LockerLoadPort;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import com.jnulocker.events.exception.EventNotFoundException;
import com.jnulocker.events.exception.LockerNotFoundException;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventQueryService implements EventQuery {

    private final EventLoadPort eventLoadPort;
    private final FloorLoadPort floorLoadPort;
    private final LockerLoadPort lockerLoadPort;
    private final MemberQuery memberQuery;

    @Override
    public Event getByIdOrThrow(UUID eventId) {
        return eventLoadPort.getById(eventId).orElseThrow(() -> EventNotFoundException.EXCEPTION);
    }

    @Override
    public EventCustomPage getAllEvents(Pageable pageable) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdWithDepartmentOrThrow(memberId);

        Page<Event> events =
                eventLoadPort.getAllEventsByDepartment(member.getDepartment(), pageable);
        return EventCustomPage.from(events);
    }

    @Override
    public EventResponse getEvent(UUID eventId) {
        Event event =
                eventLoadPort
                        .getEventByIdWithEventParticipation(eventId)
                        .orElseThrow(() -> EventNotFoundException.EXCEPTION);
        return EventResponse.from(event);
    }

    @Override
    public MyEventCustomPage getMyEvents(Pageable pageable) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        return eventLoadPort.getEventsByParticipationDepartment(member.getDepartment(), pageable);
    }

    @Override
    public Locker getLockerByIdOrThrow(Long lockerId) {
        return lockerLoadPort
                .getById(lockerId)
                .orElseThrow(() -> LockerNotFoundException.EXCEPTION);
    }

    @Override
    public List<FloorWithLockersResponse> getLockersByEventId(UUID eventId) {
        if (!eventLoadPort.existsById(eventId)) {
            throw EventNotFoundException.EXCEPTION;
        }

        List<Floor> floors = floorLoadPort.getFloorsByEventId(eventId);

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
        return lockerLoadPort.getLockersByFloorId(floor.getId()).stream()
                .map(LockerResponse::from)
                .toList();
    }
}
