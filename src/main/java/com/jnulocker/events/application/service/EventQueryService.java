package com.jnulocker.events.application.service;

import static com.jnulocker.events.infrastructure.mapper.LockerMapper.*;

import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.request.FloorInfo;
import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.EventResponse;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.LockerResponse;
import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import com.jnulocker.events.application.port.in.response.MyEventResponse;
import com.jnulocker.events.application.port.out.EventLoadPort;
import com.jnulocker.events.application.port.out.FloorLoadPort;
import com.jnulocker.events.application.port.out.LockerLoadPort;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import com.jnulocker.events.exception.EventNotFoundException;
import com.jnulocker.events.exception.LockerNotFoundException;
import com.jnulocker.events.exception.OnlyDepartmentManagerCanSeeEventException;
import com.jnulocker.events.exception.OnlyParticipationDepartmentCanSeeEventException;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        Event event =
                eventLoadPort
                        .getEventByIdWithEventParticipation(eventId)
                        .orElseThrow(() -> EventNotFoundException.EXCEPTION);

        if (!event.isSameDepartment(member.getDepartment())) {
            throw OnlyDepartmentManagerCanSeeEventException.EXCEPTION;
        }

        // 이벤트에 속한 층과 사물함 정보 조회
        List<FloorWithLockersResponse> floorWithLockers = getLockersByEventId(eventId);

        // 층 정보를 FloorInfo 형태로 변환 (LockerMapper 사용)
        List<FloorInfo> floors = toFloorInfos(floorWithLockers);

        return EventResponse.of(event, floors);
    }

    @Override
    public MyEventCustomPage getMyEvents(Pageable pageable) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        return eventLoadPort.getEventsByParticipationDepartment(member.getDepartment(), pageable);
    }

    @Override
    public MyEventResponse getMyEvent(UUID eventId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        Event event =
                eventLoadPort
                        .getEventByIdWithEventParticipation(eventId)
                        .orElseThrow(() -> EventNotFoundException.EXCEPTION);

        if (!event.isParticipationDepartment(member.getDepartment())) {
            throw OnlyParticipationDepartmentCanSeeEventException.EXCEPTION;
        }

        if (!event.getPublish()) {
            throw EventNotFoundException.EXCEPTION;
        }

        return MyEventResponse.from(event);
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
