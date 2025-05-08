package com.jnulocker.events.adapter.out;

import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.Floor;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorRepository extends JpaRepository<Floor, Long> {

    List<Floor> findAllByEventId(UUID eventId);

    void deleteAllByEvent(Event event);
}
