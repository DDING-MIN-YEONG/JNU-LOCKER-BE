package com.jnulocker.organization.application.port.in;

import com.jnulocker.organization.application.port.in.response.DepartmentResponse;
import com.jnulocker.organization.domain.Department;
import java.util.List;

public interface GetDepartmentQuery {
    List<DepartmentResponse> getDepartmentsByOrganizationId(Long organizationId);

    Department getDepartmentByIdOrThrow(Long departmentId);
}
