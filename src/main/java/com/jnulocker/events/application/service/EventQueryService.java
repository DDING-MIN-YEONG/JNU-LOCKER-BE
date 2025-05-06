package com.jnulocker.events.application.service;

import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.LockerResponse;
import com.jnulocker.events.application.port.in.response.MyEventResponse;
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
    public Event getByIdOrThrow(Long eventId) {
        return eventLoadPort.getById(eventId).orElseThrow(() -> EventNotFoundException.EXCEPTION);
    }

    @Override
    public EventCustomPage getAllEvents(Pageable pageable) {
        // TODO: MANAGER가 조회하는 경우 자신이 소속된 조직의 이벤트만 조회할 수 있도록 수정
        Page<Event> events = eventLoadPort.getAllEvents(pageable);
        return EventCustomPage.from(events);
    }

    @Override
    public List<MyEventResponse> getMyEvents() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        // 사용자의 소속학과가 참여하는 이벤트 조회
        List<Event> events =
                eventLoadPort.getEventsByParticipationDepartment(member.getDepartment());

        // 각 이벤트에 대해 사용 가능한 사물함 개수 조회
        return events.stream()
                .map(
                        event -> {
                            Integer availableLockerCount =
                                    lockerLoadPort.getAvailableLockerCountOfEvent(event);
                            return MyEventResponse.of(event, availableLockerCount);
                        })
                .toList();
    }

    @Override
    public Locker getLockerByIdOrThrow(Long lockerId) {
        return lockerLoadPort
                .getById(lockerId)
                .orElseThrow(() -> LockerNotFoundException.EXCEPTION);
    }

    @Override
    public List<FloorWithLockersResponse> getLockersByEventId(Long eventId) {
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
