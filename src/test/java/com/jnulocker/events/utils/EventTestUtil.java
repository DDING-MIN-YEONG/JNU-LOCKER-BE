package com.jnulocker.events.utils;

import static events.domain.EventTestDataBuilder.*;
import static organization.domain.DepartmentTestDataBuilder.*;
import static organization.domain.OrganizationTestDataBuilder.*;

import com.jnulocker.auth.utils.AuthTestUtil;
import com.jnulocker.events.adapter.out.EventParticipationRepository;
import com.jnulocker.events.adapter.out.EventRepository;
import com.jnulocker.events.adapter.out.FloorRepository;
import com.jnulocker.events.adapter.out.LockerRepository;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventParticipation;
import com.jnulocker.events.domain.EventStatus;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.member.utils.MemberTestUtil;
import com.jnulocker.organization.adapter.out.DepartmentRepository;
import com.jnulocker.organization.adapter.out.OrganizationRepository;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import com.jnulocker.organization.utils.OrganizationTestUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventTestUtil {

    @Autowired private OrganizationRepository organizationRepository;

    @Autowired private DepartmentRepository departmentRepository;

    @Autowired private EventRepository eventRepository;

    @Autowired private EventParticipationRepository eventParticipationRepository;

    @Autowired private FloorRepository floorRepository;

    @Autowired private LockerRepository lockerRepository;

    @Autowired private OrganizationTestUtil organizationTestUtil;

    @Autowired private MemberTestUtil memberTestUtil;

    @Autowired private AuthTestUtil authTestUtil;

    public LockerEventContext setUpLockerEventForRegistration(
            List<Integer> lockersPerFloor, Role role, EventStatus eventStatus, boolean publish) {
        // Department 생성
        Department department = organizationTestUtil.createCouncilDepartment();

        // Member 생성
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(role, department);

        // AccessToken 생성
        String accessToken = authTestUtil.generateAccessTokenWithMember(member);

        // Event 생성 및 Department를 EventParticipation에 추가
        Event event =
                createEventWithParticipationDepartment(
                        lockersPerFloor, department, eventStatus, publish);

        return new LockerEventContext(department, member, accessToken, event);
    }

    public LockerEventContext setupLockerEventWithParticipatingDepartment(
            List<Integer> lockersPerFloor,
            Role role,
            EventStatus eventStatus,
            boolean publish,
            List<Department> participationDepartments) {
        // Department 생성 (사용자의 Department)
        Department userDepartment = organizationTestUtil.createCouncilDepartment();

        // Member 생성
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(role, userDepartment);

        // AccessToken 생성
        String accessToken = authTestUtil.generateAccessTokenWithMember(member);

        // Event 생성
        Event event = createEventWithFloorAndLockers(lockersPerFloor, eventStatus, publish);

        // 지정된 Department들만 EventParticipation에 추가: 사용자의 Department는 제외
        for (Department dept : participationDepartments) {
            addParticipationDepartment(event, dept);
        }

        return new LockerEventContext(userDepartment, member, accessToken, event);
    }

    private void addParticipationDepartment(Event event, Department department) {
        EventParticipation participation = EventParticipation.create(event, department);
        eventParticipationRepository.save(participation);
    }

    public Event createEventWithParticipationDepartment(
            List<Integer> lockersPerFloor,
            Department department,
            EventStatus eventStatus,
            boolean publish) {

        // 이벤트 생성 및 저장
        Event savedEvent = createAndSaveEvent(department, eventStatus, publish);

        // 이벤트 참여 학과 정보 생성 및 저장
        EventParticipation eventParticipation = EventParticipation.create(savedEvent, department);
        eventParticipationRepository.save(eventParticipation);

        // 층 생성 및 저장
        createFloorsWithEvent(lockersPerFloor, savedEvent);
        return savedEvent;
    }

    public Event createEventWithFloorAndLockers(
            List<Integer> lockersPerFloor, EventStatus eventStatus, boolean publish) {
        // 조직 생성 및 저장
        Organization organization = organizationBuilder().build();
        Organization savedOrganization = organizationRepository.save(organization);

        // 학과 생성 및 저장
        Department department = departmentBuilder().withOrganization(savedOrganization).build();
        Department savedDepartment = departmentRepository.save(department);

        // 이벤트 생성 및 저장
        Event savedEvent = createAndSaveEvent(savedDepartment, eventStatus, publish);

        // 층 생성 및 저장
        createFloorsWithEvent(lockersPerFloor, savedEvent);

        return savedEvent;
    }

    private Event createAndSaveEvent(
            Department department, EventStatus eventStatus, boolean publish) {
        Event event =
                eventBuilder()
                        .withDepartment(department)
                        .withEventStatus(eventStatus)
                        .withPublish(publish)
                        .build();
        return eventRepository.save(event);
    }

    private void createFloorsWithEvent(List<Integer> lockersPerFloor, Event savedEvent) {
        for (int floorLevel = 0; floorLevel < lockersPerFloor.size(); floorLevel++) {
            int floorNumber = floorLevel + 1;
            Floor floor = floorRepository.save(Floor.create(savedEvent, floorNumber));
            createLockersWithFloor(lockersPerFloor, floorLevel, floor);
        }
    }

    private void createLockersWithFloor(
            List<Integer> lockersPerFloor, int floorLevel, Floor floor) {
        List<Locker> lockers = new ArrayList<>();
        for (int j = 1; j <= lockersPerFloor.get(floorLevel); j++) {
            String code =
                    String.format("%c-%03d", 'A' + floorLevel, j); // A-001, A-002, B-001, B-002 등
            boolean available = j % 2 == 0; // 짝수 번호 사물함은 사용 가능, 홀수는 사용 불가능으로 설정

            lockers.add(Locker.create(floor, code, available));
        }
        lockerRepository.saveAll(lockers);
    }

    public void deleteAll() {
        lockerRepository.deleteAll();
        floorRepository.deleteAll();
        eventParticipationRepository.deleteAll();
        eventRepository.deleteAll();
        departmentRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    public Event getEventById(UUID eventId) {
        return eventRepository.findById(eventId).orElseThrow();
    }

    public LockerEventContext setUpLockerEventWithCustomCodes(
            Map<Integer, List<String>> floorToLockerCodes,
            Role role,
            EventStatus eventStatus,
            boolean publish) {
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(role, department);
        String accessToken = authTestUtil.generateAccessTokenWithMember(member);

        Event event =
                createEventWithCustomCodes(floorToLockerCodes, department, eventStatus, publish);

        return new LockerEventContext(department, member, accessToken, event);
    }

    public LockerEventContext setUpLockerEventWithMixedPattern(
            Map<Integer, List<String>> floorToLockerCodes,
            Role role,
            EventStatus eventStatus,
            boolean publish) {
        return setUpLockerEventWithCustomCodes(floorToLockerCodes, role, eventStatus, publish);
    }

    private Event createEventWithCustomCodes(
            Map<Integer, List<String>> floorToLockerCodes,
            Department department,
            EventStatus eventStatus,
            boolean publish) {
        Event savedEvent = createAndSaveEvent(department, eventStatus, publish);
        EventParticipation eventParticipation = EventParticipation.create(savedEvent, department);
        eventParticipationRepository.save(eventParticipation);

        createFloorsWithCustomCodes(floorToLockerCodes, savedEvent);
        return savedEvent;
    }

    private void createFloorsWithCustomCodes(
            Map<Integer, List<String>> floorToLockerCodes, Event savedEvent) {
        for (Map.Entry<Integer, List<String>> entry : floorToLockerCodes.entrySet()) {
            int floorNumber = entry.getKey();
            List<String> lockerCodes = entry.getValue();
            Floor floor = floorRepository.save(Floor.create(savedEvent, floorNumber));
            createLockersWithCustomCodes(lockerCodes, floor);
        }
    }

    private void createLockersWithCustomCodes(List<String> lockerCodes, Floor floor) {
        List<Locker> lockers = new ArrayList<>();
        for (int i = 0; i < lockerCodes.size(); i++) {
            String code = lockerCodes.get(i);
            boolean available = (i + 1) % 2 == 0;
            lockers.add(Locker.create(floor, code, available));
        }
        lockerRepository.saveAll(lockers);
    }
}
