package com.jnulocker.organization.application.service;

import com.jnulocker.organization.application.port.in.DepartmentQuery;
import com.jnulocker.organization.application.port.in.OrganizationQuery;
import com.jnulocker.organization.application.port.in.response.DepartmentResponse;
import com.jnulocker.organization.application.port.in.response.OrganizationResponse;
import com.jnulocker.organization.application.port.out.DepartmentLoadPort;
import com.jnulocker.organization.application.port.out.OrganizationLoadPort;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.OrganizationType;
import com.jnulocker.organization.exception.DepartmentNotFoundException;
import com.jnulocker.organization.exception.OrganizationNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationQueryService implements OrganizationQuery, DepartmentQuery {
    private final OrganizationLoadPort organizationLoadPort;
    private final DepartmentLoadPort departmentLoadPort;

    @Override
    public List<OrganizationResponse> getOrganizations(OrganizationType organizationType) {
        return organizationLoadPort.getAllByType(organizationType).stream()
                .map(OrganizationResponse::from)
                .toList();
    }

    @Override
    public List<DepartmentResponse> getDepartmentsByOrganizationId(Long organizationId) {
        if (!organizationLoadPort.existsById(organizationId)) {
            throw OrganizationNotFoundException.EXCEPTION;
        }

        return departmentLoadPort.getDepartmentsByOrganizationId(organizationId).stream()
                .map(DepartmentResponse::from)
                .toList();
    }

    @Override
    public Department getDepartmentByIdOrThrow(Long departmentId) {
        return departmentLoadPort
                .getDepartmentById(departmentId)
                .orElseThrow(() -> DepartmentNotFoundException.EXCEPTION);
    }

    @Override
    public List<Department> getDepartmentsByIdIn(List<Long> departmentIds) {
        return departmentLoadPort.getDepartmentsByIdIn(departmentIds);
    }
}
