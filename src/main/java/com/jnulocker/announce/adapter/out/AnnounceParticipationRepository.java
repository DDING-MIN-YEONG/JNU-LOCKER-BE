package com.jnulocker.announce.adapter.out;

import com.jnulocker.announce.domain.AnnounceParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceParticipationRepository
        extends JpaRepository<AnnounceParticipation, Long> {}
