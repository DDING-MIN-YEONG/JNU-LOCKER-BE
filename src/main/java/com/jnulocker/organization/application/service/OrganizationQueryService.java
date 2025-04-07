package com.jnulocker.organization.application.service;

import com.jnulocker.organization.application.port.in.GetDepartmentQuery;
import com.jnulocker.organization.application.port.in.GetOrganizationQuery;
import com.jnulocker.organization.application.port.in.response.DepartmentResponse;
import com.jnulocker.organization.application.port.in.response.OrganizationResponse;
import com.jnulocker.organization.application.port.out.DepartmentLoadPort;
import com.jnulocker.organization.application.port.out.OrganizationLoadPort;
import com.jnulocker.organization.domain.OrganizationType;
import com.jnulocker.organization.exception.OrganizationNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationQueryService implements GetOrganizationQuery, GetDepartmentQuery {
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
}
