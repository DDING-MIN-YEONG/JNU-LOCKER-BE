package com.jnulocker.events.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.events.application.port.out.EventLoadPort;
import com.jnulocker.events.application.port.out.EventRecordPort;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventParticipation;
import com.jnulocker.events.domain.Floor;
import com.jnulocker.events.domain.Locker;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@PersistenceAdapter
@RequiredArgsConstructor
public class EventPersistenceAdapter implements EventLoadPort, EventRecordPort {

    private final EventRepository eventRepository;
    private final EventParticipationRepository eventParticipationRepository;
    private final FloorRepository floorRepository;
    private final LockerRepository lockerRepository;

    @Override
    public boolean existsById(Long eventId) {
        return eventRepository.existsById(eventId);
    }

    @Override
    public Optional<Event> getById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Override
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void saveEventParticipations(List<EventParticipation> eventParticipations) {
        eventParticipationRepository.saveAll(eventParticipations);
    }

    @Override
    public Floor saveFloor(Floor floor) {
        return floorRepository.save(floor);
    }

    @Override
    public void saveLockers(List<Locker> lockers) {
        lockerRepository.saveAll(lockers);
    }

    @Override
    public void deleteEvent(Event event) {
        lockerRepository.deleteAllByFloor_Event(event);
        floorRepository.deleteAllByEvent(event);
        eventParticipationRepository.deleteAllByEvent(event);
        eventRepository.delete(event);
    }
}
