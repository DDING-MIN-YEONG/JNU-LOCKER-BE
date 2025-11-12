package com.jnulocker.events.application.service;

import static com.jnulocker.events.infrastructure.mapper.LockerMapper.*;

import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.request.FloorInfo;
import com.jnulocker.events.application.port.in.response.AvailableLockersResponse;
import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.EventResponse;
import com.jnulocker.events.application.port.in.response.FloorSummary;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.LockerResponse;
import com.jnulocker.events.application.port.in.response.LockerSummaryResponse;
import com.jnulocker.events.application.port.in.response.LockerWithFloorInfo;
import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import com.jnulocker.events.application.port.in.response.MyEventResponse;
import com.jnulocker.events.application.port.in.response.PagedLockersResponse;
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
import java.util.stream.Collectors;
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

        Event event = getByIdWithEventParticipationOrThrow(eventId);

        if (!event.isSameDepartment(member.getDepartment())) {
            throw OnlyDepartmentManagerCanSeeEventException.EXCEPTION;
        }

        // 이벤트에 속한 층과 사물함 정보 조회
        List<FloorWithLockersResponse> floorWithLockers = getLockersByEventId(eventId);

        // 층 정보를 FloorInfo 형태로 변환 (LockerMapper 사용)
        List<FloorInfo> floors = toFloorInfos(floorWithLockers);

        return EventResponse.of(event, floors);
    }

    private Event getByIdWithEventParticipationOrThrow(UUID eventId) {
        return eventLoadPort
                .getEventByIdWithEventParticipation(eventId)
                .orElseThrow(() -> EventNotFoundException.EXCEPTION);
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

        Event event = getByIdWithEventParticipationOrThrow(eventId);

        if (!event.isParticipationDepartment(member.getDepartment())) {
            throw OnlyParticipationDepartmentCanSeeEventException.EXCEPTION;
        }

        event.validatePublishStatus();

        return MyEventResponse.from(event);
    }

    @Override
    public Locker getLockerByIdOrThrow(UUID lockerId) {
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
        if (floors.isEmpty()) {
            return List.of();
        }

        // 모든 사물함을 가져와서 층별로 그룹화
        List<Locker> allLockers = lockerLoadPort.getLockersByFloorIds(floors);
        Map<UUID, List<Locker>> lockersByFloorId =
                allLockers.stream()
                        .collect(Collectors.groupingBy(locker -> locker.getFloor().getId()));

        return floors.stream()
                .map(floor -> convertToFloorResponse(floor, lockersByFloorId))
                .toList();
    }

    private FloorWithLockersResponse convertToFloorResponse(
            Floor floor, Map<UUID, List<Locker>> lockersByFloorId) {
        List<Locker> lockersForFloor =
                lockersByFloorId.getOrDefault(floor.getId(), List.of()).stream().sorted().toList();
        return FloorWithLockersResponse.of(floor, lockersForFloor);
    }

    @Override
    public LockerSummaryResponse getLockerSummary(UUID eventId) {
        List<FloorWithLockersResponse> floors = getLockersByEventId(eventId);

        int totalLockers = 0;
        int availableLockers = 0;
        List<FloorSummary> floorSummaries = new ArrayList<>();

        for (FloorWithLockersResponse floor : floors) {
            int floorTotal = floor.lockers().size();
            long floorAvailable =
                    floor.lockers().stream().filter(LockerResponse::available).count();

            totalLockers += floorTotal;
            availableLockers += (int) floorAvailable;

            floorSummaries.add(
                    FloorSummary.of(floor.floorNumber(), floorTotal, (int) floorAvailable));
        }

        return LockerSummaryResponse.of(totalLockers, availableLockers, floorSummaries);
    }

    @Override
    public PagedLockersResponse getLockersByFloor(
            UUID eventId, Integer floorNumber, Pageable pageable) {
        List<FloorWithLockersResponse> floors = getLockersByEventId(eventId);

        FloorWithLockersResponse targetFloor =
                floors.stream()
                        .filter(f -> f.floorNumber().equals(floorNumber))
                        .findFirst()
                        .orElse(null);

        if (targetFloor == null) {
            return PagedLockersResponse.of(List.of(), 0, 0, 0, "해당 층을 찾을 수 없습니다: " + floorNumber);
        }

        List<LockerResponse> allLockers = targetFloor.lockers();
        int pageNum = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        int totalElements = allLockers.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        int start = pageNum * pageSize;
        int end = Math.min(start + pageSize, totalElements);

        List<LockerResponse> pagedLockers =
                start < totalElements ? allLockers.subList(start, end) : List.of();

        return PagedLockersResponse.of(
                pagedLockers, pageNum, totalPages, totalElements, floorNumber + "층");
    }

    @Override
    public AvailableLockersResponse getAvailableLockers(UUID eventId, Pageable pageable) {
        List<FloorWithLockersResponse> floors = getLockersByEventId(eventId);

        List<LockerWithFloorInfo> availableLockers =
                floors.stream()
                        .flatMap(
                                floor ->
                                        floor.lockers().stream()
                                                .filter(LockerResponse::available)
                                                .map(
                                                        locker ->
                                                                LockerWithFloorInfo.of(
                                                                        locker.lockerId(),
                                                                        locker.code(),
                                                                        floor.floorNumber())))
                        .toList();

        int pageNum = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        int totalElements = availableLockers.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        int start = pageNum * pageSize;
        int end = Math.min(start + pageSize, totalElements);

        List<LockerWithFloorInfo> pagedLockers =
                start < totalElements ? availableLockers.subList(start, end) : List.of();

        return AvailableLockersResponse.of(pagedLockers, pageNum, totalPages, totalElements);
    }
}
