package com.jnulocker.events.utils;

import com.jnulocker.events.adapter.out.EventRepository;
import com.jnulocker.events.adapter.out.FloorRepository;
import com.jnulocker.events.adapter.out.LockerRepository;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import com.jnulocker.organization.adapter.out.OrganizationRepository;
import com.jnulocker.organization.domain.Organization;
import events.builder.EventTestDataBuilder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import organization.builder.OrganizationTestDataBuilder;

@Component
public class EventTestUtil {

    @Autowired private OrganizationRepository organizationRepository;

    @Autowired private EventRepository eventRepository;

    @Autowired private FloorRepository floorRepository;

    @Autowired private LockerRepository lockerRepository;

    public Event createEventWithFloorAndLockers(List<Integer> lockersPerFloor) {
        // 조직 생성 및 저장
        Organization organization = OrganizationTestDataBuilder.builder().build();
        Organization savedOrganization = organizationRepository.save(organization);

        // 이벤트 생성 및 저장
        Event event = EventTestDataBuilder.builder().withOrganization(savedOrganization).build();
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
}
