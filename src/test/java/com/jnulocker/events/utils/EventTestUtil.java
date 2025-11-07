package com.jnulocker.events.utils;

import static events.domain.EventTestDataBuilder.eventBuilder;
import static organization.domain.DepartmentTestDataBuilder.departmentBuilder;
import static organization.domain.OrganizationTestDataBuilder.organizationBuilder;

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
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(role, department);
        String accessToken = authTestUtil.generateAccessTokenWithMember(member);

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
        Department userDepartment = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(role, userDepartment);
        String accessToken = authTestUtil.generateAccessTokenWithMember(member);

        Event event = createEventWithFloorAndLockers(lockersPerFloor, eventStatus, publish);

        participationDepartments.forEach(dept -> addDepartmentToEvent(event, dept));

        return new LockerEventContext(userDepartment, member, accessToken, event);
    }

    public Event createEventWithParticipationDepartment(
            List<Integer> lockersPerFloor,
            Department department,
            EventStatus eventStatus,
            boolean publish) {
        Event savedEvent = createAndSaveEvent(department, eventStatus, publish);

        addDepartmentToEvent(savedEvent, department);

        createFloorsWithEvent(lockersPerFloor, savedEvent);
        return savedEvent;
    }

    public Event createEventWithFloorAndLockers(
            List<Integer> lockersPerFloor, EventStatus eventStatus, boolean publish) {
        Department department = createDepartmentWithOrganization();
        Event savedEvent = createAndSaveEvent(department, eventStatus, publish);
        createFloorsWithEvent(lockersPerFloor, savedEvent);

        return savedEvent;
    }

    private Department createDepartmentWithOrganization() {
        Organization organization = organizationBuilder().build();
        Organization savedOrganization = organizationRepository.save(organization);

        Department department = departmentBuilder().withOrganization(savedOrganization).build();
        return departmentRepository.save(department);
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
            createLockersForFloor(lockersPerFloor.get(floorLevel), floorLevel, floor);
        }
    }

    private void createLockersForFloor(int lockerCount, int floorLevel, Floor floor) {
        List<Locker> lockers = new ArrayList<>();
        for (int j = 1; j <= lockerCount; j++) {
            String code = String.format("%c-%03d", 'A' + floorLevel, j);
            boolean available = j % 2 == 0;

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
                createEventWithCustomLockerCodes(
                        floorToLockerCodes, department, eventStatus, publish);

        return new LockerEventContext(department, member, accessToken, event);
    }

    private Event createEventWithCustomLockerCodes(
            Map<Integer, List<String>> floorToLockerCodes,
            Department department,
            EventStatus eventStatus,
            boolean publish) {
        Event savedEvent = createAndSaveEvent(department, eventStatus, publish);
        addDepartmentToEvent(savedEvent, department);

        createFloorsWithCustomCodes(floorToLockerCodes, savedEvent);
        return savedEvent;
    }

    private void createFloorsWithCustomCodes(
            Map<Integer, List<String>> floorToLockerCodes, Event savedEvent) {
        floorToLockerCodes.forEach(
                (floorNumber, lockerCodes) -> {
                    Floor floor = floorRepository.save(Floor.create(savedEvent, floorNumber));
                    createLockersWithCustomCodes(lockerCodes, floor);
                });
    }

    private void createLockersWithCustomCodes(List<String> lockerCodes, Floor floor) {
        List<Locker> lockers = new ArrayList<>();
        for (int i = 0; i < lockerCodes.size(); i++) {
            boolean available = (i + 1) % 2 == 0;
            lockers.add(Locker.create(floor, lockerCodes.get(i), available));
        }
        lockerRepository.saveAll(lockers);
    }

    public void addDepartmentToEvent(Event event, Department department) {
        EventParticipation participation = EventParticipation.create(event, department);
        eventParticipationRepository.save(participation);
    }
}
