package com.jnulocker.announce.adapter.out;

import com.jnulocker.announce.domain.Announce;
import com.jnulocker.organization.domain.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceRepository extends JpaRepository<Announce, Long> {

    Page<Announce> findAllByDepartment(Department department, Pageable pageable);
}
