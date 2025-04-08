package com.jnulocker.events.application.service;

import com.jnulocker.events.application.port.in.EventCommand;
import com.jnulocker.events.application.port.in.request.CreateEventRequest;
import com.jnulocker.events.application.port.in.request.FloorInfo;
import com.jnulocker.events.application.port.out.EventRecordPort;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventParticipation;
import com.jnulocker.events.domain.EventStatus;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import com.jnulocker.events.event.LockerEventCreatedEvent;
import com.jnulocker.organization.application.port.in.DepartmentQuery;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.exception.DepartmentNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCommandService implements EventCommand {

    private final ApplicationEventPublisher eventPublisher;
    private final DepartmentQuery departmentQuery;
    private final EventRecordPort eventRecordPort;

    @Override
    @Transactional
    public void save(Event event) {
        eventRecordPort.saveEvent(event);
    }

    @Override
    @Transactional
    public void createEvent(CreateEventRequest request) {
        // 이벤트 생성 및 저장
        Event savedEvent = createAndSaveEvent(request);

        // 이벤트 참여 학과 정보 생성 및 저장
        setupEventParticipations(savedEvent, request.participationDepartmentIds());

        // 층 및 사물함 생성 및 저장
        createFloorsAndLockers(savedEvent, request.floors());

        // 사물함 이벤트 생성 이벤트 발행
        publishEventCreatedEvent(savedEvent);
    }

    private Event createAndSaveEvent(CreateEventRequest request) {
        // 이벤트를 주최하는 department 조회
        // TODO: 회원 정보를 통해 MANAGER 소속의 departmentId를 가져오는 로직으로 변경
        Department organizerDepartment =
                departmentQuery.getDepartmentByIdOrThrow(request.departmentId());

        // 이벤트 생성 및 저장
        return eventRecordPort.saveEvent(
                Event.create(
                        request.title(),
                        organizerDepartment,
                        request.startAt(),
                        request.endAt(),
                        EventStatus.READY,
                        false));
    }

    private void setupEventParticipations(Event event, List<Long> participationDepartmentIds) {
        // 이벤트에 참여하는 학과 조회
        List<Department> departments =
                departmentQuery.getDepartmentsByIdIn(participationDepartmentIds);
        if (departments.size() != participationDepartmentIds.size()) {
            throw DepartmentNotFoundException.EXCEPTION;
        }

        // 이벤트 참여 정보 생성 및 저장
        List<EventParticipation> participations =
                departments.stream()
                        .map(department -> EventParticipation.create(event, department))
                        .toList();
        eventRecordPort.saveEventParticipations(participations);
    }

    private void createFloorsAndLockers(Event event, List<FloorInfo> floorInfos) {
        for (FloorInfo floorInfo : floorInfos) {
            // 층 생성 및 저장
            Floor floor = eventRecordPort.saveFloor(Floor.create(event, floorInfo.floorNumber()));

            // 층에 속한 사물함 생성 및 저장
            List<Locker> lockers = createLockersForFloor(floorInfo, floor);
            eventRecordPort.saveLockers(lockers);
        }
    }

    private List<Locker> createLockersForFloor(FloorInfo floorInfo, Floor floor) {
        List<Locker> lockers = new ArrayList<>();
        for (int i = floorInfo.lockerStartNumber(); i <= floorInfo.lockerEndNumber(); i++) {
            String code = generateLockerCode(floorInfo.lockerPrefix(), i);
            lockers.add(Locker.create(floor, code, true));
        }
        return lockers;
    }

    // 사물함 코드 생성 접두사 prefix가 null이거나 비어있으면 3자리 숫자만 생성 그렇지 않으면 prefix-3자리 숫자 형태로 생성
    private String generateLockerCode(String prefix, int number) {
        if (prefix != null && !prefix.isBlank()) {
            return String.format("%s-%03d", prefix, number); // A-001
        } else {
            return String.format("%03d", number); // 001
        }
    }

    private void publishEventCreatedEvent(Event event) {
        eventPublisher.publishEvent(
                LockerEventCreatedEvent.of(
                        event.getId(),
                        event.getEventSchedule().getStartAt(),
                        event.getEventSchedule().getEndAt()));
    }
}
