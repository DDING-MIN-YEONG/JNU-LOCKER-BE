package com.jnulocker.announce.application.port.out;

import com.jnulocker.announce.domain.Announce;
import com.jnulocker.organization.domain.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnounceLoadPort {

    Page<Announce> getAllAnnouncesByDepartment(Department department, Pageable pageable);
}
