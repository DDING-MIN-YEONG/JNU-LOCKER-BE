package com.jnulocker.organization.application.port.out;

import com.jnulocker.organization.domain.Department;
import java.util.List;

public interface DepartmentLoadPort {

    List<Department> getDepartmentsByOrganizationId(Long organizationId);

    Department getById(Long id);
}
