package com.jnulocker.announce.application.port.in;

import com.jnulocker.announce.application.port.in.response.AnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.MyAnnounceResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AnnounceQuery {

    AnnounceCustomPage getAllAnnounces(Pageable pageable);

    List<MyAnnounceResponse> getMyAnnounces();
}
