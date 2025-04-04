package com.jnulocker.organization.application.service;

import com.jnulocker.organization.application.port.in.OrganizationUseCase;
import com.jnulocker.organization.application.port.out.OrganizationLoadPort;
import com.jnulocker.organization.domain.Organization;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService implements OrganizationUseCase {
    private final OrganizationLoadPort organizationLoadPort;

    @Override
    public Set<String> findAffiliations() {
        List<Organization> organizations = organizationLoadPort.findAll();
        return organizations.stream().map(Organization::getAffiliation).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public List<String> findDepartments(String affiliation) {
        List<Organization> organizations = organizationLoadPort.loadByAffiliation(affiliation);
        return organizations.stream()
                .map(Organization::getDepartment)
                .filter(Objects::nonNull)
                .toList();
    }
}
