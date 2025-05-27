package com.jnulocker.events.adapter.out;

import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.Locker;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LockerRepository extends JpaRepository<Locker, UUID> {

    List<Locker> findAllByFloorIdOrderByCode(UUID floorId);

    @Query("SELECT l FROM Locker l JOIN FETCH l.floor f JOIN FETCH f.event WHERE l.id = :lockerId")
    Optional<Locker> findByIdWithFloorAndEvent(UUID lockerId);

    void deleteAllByFloor_Event(Event event);
}
