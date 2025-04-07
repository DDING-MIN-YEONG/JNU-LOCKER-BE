package com.jnulocker.organization.application.port.in.response;

import com.jnulocker.organization.domain.Department;

public record DepartmentResponse(Long id, Long organizationId, String name) {

    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(
                department.getId(), department.getOrganization().getId(), department.getName());
    }
}
