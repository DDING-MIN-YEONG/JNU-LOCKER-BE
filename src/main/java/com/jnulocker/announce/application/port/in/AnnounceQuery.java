package com.jnulocker.announce.application.port.in;

import com.jnulocker.announce.application.port.in.response.AnnounceCustomPage;
import org.springframework.data.domain.Pageable;

public interface AnnounceQuery {

    AnnounceCustomPage getAllAnnounces(Pageable pageable);
}
