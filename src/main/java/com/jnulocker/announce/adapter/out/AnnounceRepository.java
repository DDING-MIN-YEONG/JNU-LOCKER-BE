package com.jnulocker.announce.adapter.out;

import com.jnulocker.announce.domain.Announce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceRepository extends JpaRepository<Announce, Long> {}
