package com.jnulocker.auth.application.port.in;

import com.jnulocker.auth.application.port.in.response.PendingManagerCustomPage;
import org.springframework.data.domain.Pageable;

public interface ManagerQuery {

    PendingManagerCustomPage getPendingManagers(Pageable pageable);
}
