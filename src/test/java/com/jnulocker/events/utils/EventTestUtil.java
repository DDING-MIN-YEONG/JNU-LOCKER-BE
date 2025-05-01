package com.jnulocker.events.utils;

import static events.domain.EventTestDataBuilder.*;
import static organization.domain.DepartmentTestDataBuilder.*;
import static organization.domain.OrganizationTestDataBuilder.*;

import com.jnulocker.events.adapter.out.EventParticipationRepository;
import com.jnulocker.events.adapter.out.EventRepository;
import com.jnulocker.events.adapter.out.FloorRepository;
import com.jnulocker.events.adapter.out.LockerRepository;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventStatus;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import com.jnulocker.organization.adapter.out.DepartmentRepository;
import com.jnulocker.organization.adapter.out.OrganizationRepository;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import java.util.ArrayList;
import java.util.List;
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

    public Event createEventWithFloorAndLockers(
            List<Integer> lockersPerFloor, EventStatus eventStatus, boolean publish) {
        // 조직 생성 및 저장
        Organization organization = organizationBuilder().build();
        Organization savedOrganization = organizationRepository.save(organization);

        // 학과 생성 및 저장
        Department department = departmentBuilder().withOrganization(savedOrganization).build();
        Department savedDepartment = departmentRepository.save(department);

        // 이벤트 생성 및 저장
        Event event =
                eventBuilder()
                        .withDepartment(savedDepartment)
                        .withEventStatus(eventStatus)
                        .withPublish(publish)
                        .build();
        Event savedEvent = eventRepository.save(event);

        // 층 생성 및 저장
        createFloorsWithEvent(lockersPerFloor, savedEvent);

        return savedEvent;
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
}
