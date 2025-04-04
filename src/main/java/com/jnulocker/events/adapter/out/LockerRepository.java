package com.jnulocker.events.adapter.out;

import com.jnulocker.events.domain.Locker;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockerRepository extends JpaRepository<Locker, Long> {

    List<Locker> findAllByFloorId(Long floorId);
}
