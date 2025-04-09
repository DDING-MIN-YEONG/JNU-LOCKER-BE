package com.jnulocker.organization.adapter.in;

import com.jnulocker.organization.adapter.in.docs.OrganizationApi;
import com.jnulocker.organization.application.port.in.DepartmentQuery;
import com.jnulocker.organization.application.port.in.OrganizationQuery;
import com.jnulocker.organization.application.port.in.response.DepartmentResponse;
import com.jnulocker.organization.application.port.in.response.OrganizationResponse;
import com.jnulocker.organization.domain.OrganizationType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController implements OrganizationApi {
    private final OrganizationQuery organizationQuery;
    private final DepartmentQuery departmentQuery;

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getOrganizations(
            @RequestParam OrganizationType type) {
        List<OrganizationResponse> organizations = organizationQuery.getOrganizations(type);
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{organization-id}/departments")
    public ResponseEntity<List<DepartmentResponse>> getDepartments(
            @PathVariable("organization-id") Long organizationId) {
        List<DepartmentResponse> departmentNames =
                departmentQuery.getDepartmentsByOrganizationId(organizationId);
        return ResponseEntity.ok(departmentNames);
    }
}
