package com.jnulocker.events.application.service;

import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.events.application.port.in.EventCommand;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.request.CreateEventRequest;
import com.jnulocker.events.application.port.in.request.FloorInfo;
import com.jnulocker.events.application.port.in.request.LockerRange;
import com.jnulocker.events.application.port.in.request.PrefixInfo;
import com.jnulocker.events.application.port.in.request.PublishEventRequest;
import com.jnulocker.events.application.port.in.request.UpdateEventRequest;
import com.jnulocker.events.application.port.out.EventRecordPort;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventParticipation;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import com.jnulocker.events.event.LockerEventCreatedEvent;
import com.jnulocker.events.event.LockerEventDeletedEvent;
import com.jnulocker.events.event.LockerEventUpdatedEvent;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import com.jnulocker.organization.application.port.in.DepartmentQuery;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.exception.DepartmentNotFoundException;
import com.jnulocker.registration.application.port.in.RegistrationCommand;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCommandService implements EventCommand {

    private final ApplicationEventPublisher eventPublisher;
    private final MemberQuery memberQuery;
    private final DepartmentQuery departmentQuery;
    private final EventQuery eventQuery;
    private final EventRecordPort eventRecordPort;
    private final RegistrationCommand registrationCommand;

    @Override
    @Transactional
    public void save(Event event) {
        eventRecordPort.saveEvent(event);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID eventId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        Event event = eventQuery.getByIdOrThrow(eventId);
        event.validateDeletable(member.getDepartment());

        eventRecordPort.deleteEvent(event);

        // 이벤트 삭제 이벤트 발행
        eventPublisher.publishEvent(LockerEventDeletedEvent.of(event.getId()));
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

    @Override
    @Transactional
    public void publishEvent(UUID eventId, PublishEventRequest request) {
        Event event = eventQuery.getByIdOrThrow(eventId);
        event.updatePublishStatus(request.isPublish());
    }

    @Override
    @Transactional
    public void updateEvent(UUID eventId, UpdateEventRequest request) {
        // 이벤트 조회 및 권한 검증
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        Event event = eventQuery.getByIdOrThrow(eventId);
        event.validateUpdatable(member.getDepartment());

        // 이벤트 기본 정보 업데이트
        event.updateInfo(request.title(), request.startAt(), request.endAt());

        // 이벤트와 연관된 참여, 사물함, 층 정보 삭제
        eventRecordPort.deleteEventRelations(event);

        // 이벤트 참여 학과 정보 업데이트
        setupEventParticipations(event, request.participationDepartmentIds());

        // 층 및 사물함 정보 업데이트
        createFloorsAndLockers(event, request.floors());

        publishEventUpdatedEvent(event);
    }

    private Event createAndSaveEvent(CreateEventRequest request) {
        // 이벤트를 주최하는 department 조회
        Long memberId = SecurityUtils.getCurrentMemberId();
        Department organizerDepartment =
                memberQuery.findByIdWithDepartmentOrThrow(memberId).getDepartment();

        // 이벤트 생성 및 저장
        return eventRecordPort.saveEvent(
                Event.create(
                        request.title(), organizerDepartment, request.startAt(), request.endAt()));
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
        for (PrefixInfo prefixInfo : floorInfo.prefixes()) {
            for (LockerRange range : prefixInfo.ranges()) {
                for (int i = range.lockerStartNumber(); i <= range.lockerEndNumber(); i++) {
                    String code = generateLockerCode(prefixInfo.lockerPrefix(), i);
                    lockers.add(Locker.create(floor, code, true));
                }
            }
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

    private void publishEventUpdatedEvent(Event event) {
        eventPublisher.publishEvent(
                LockerEventUpdatedEvent.of(
                        event.getId(),
                        event.getEventSchedule().getStartAt(),
                        event.getEventSchedule().getEndAt()));
    }
}
