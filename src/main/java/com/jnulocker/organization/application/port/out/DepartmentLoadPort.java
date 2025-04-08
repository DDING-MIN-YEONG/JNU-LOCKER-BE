package com.jnulocker.organization.application.port.out;

import com.jnulocker.organization.domain.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentLoadPort {

    List<Department> getDepartmentsByOrganizationId(Long organizationId);

    Optional<Department> getDepartmentById(Long departmentId);
}
