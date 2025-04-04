package com.jnulocker.events.adapter.out;

import com.jnulocker.events.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}
