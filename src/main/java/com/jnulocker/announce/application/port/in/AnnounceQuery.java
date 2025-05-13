package com.jnulocker.announce.application.port.in;

import com.jnulocker.announce.application.port.in.response.AnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.AnnounceDetailResponse;
import com.jnulocker.announce.application.port.in.response.MyAnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.MyAnnounceDetailResponse;
import com.jnulocker.announce.domain.Announce;
import com.jnulocker.announce.domain.AnnounceParticipation;
import com.jnulocker.organization.domain.Department;
import org.springframework.data.domain.Pageable;

public interface AnnounceQuery {

    AnnounceCustomPage getAnnounces(Pageable pageable);

    MyAnnounceCustomPage getMyAnnounces(Pageable pageable);

    Announce getByIdOrThrow(Long announceId);

    AnnounceDetailResponse getAnnounce(Long announceId);

    MyAnnounceDetailResponse getMyAnnounce(Long announceId);

    Announce getByIdAndDepartmentOrThrow(Long announceId, Department department);

    AnnounceParticipation getByAnnounceIdAndDepartmentOrThrow(
            Long announceId, Department department);
}
