package com.jnulocker.organization.application.port.out;

import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import java.util.List;

public interface DepartmentLoadPort {

    List<Department> getDepartmentsByOrganizationId(Long organizationId);

    Department getByOrganizationAndId(Organization organization, Long Id);
}
