package com.jnulocker.events.adapter.out;

import com.jnulocker.events.domain.Floor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorRepository extends JpaRepository<Floor, Long> {

    List<Floor> findAllByEventId(Long eventId);
}
