package com.jnulocker.organization.adapter.out;

import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByOrganizationId(Long organizationId);

    //    Department findByOrganizationAndName(Organization organization, String name);
    Department findByOrganizationAndId(Organization organization, Long id);
}
