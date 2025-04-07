package com.jnulocker.organization.adapter.out;

import com.jnulocker.organization.domain.Department;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByOrganizationId(Long organizationId);
}
