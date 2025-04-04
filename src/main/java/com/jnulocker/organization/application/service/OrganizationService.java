package com.jnulocker.organization.application.service;

import com.jnulocker.organization.application.port.in.OrganizationUseCase;
import com.jnulocker.organization.application.port.out.OrganizationLoadPort;
import com.jnulocker.organization.domain.Organization;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService implements OrganizationUseCase {
    private final OrganizationLoadPort organizationLoadPort;

    @Override
    public List<Organization> findAll() {
        return organizationLoadPort.findAll();
    }

    @Override
    public Set<String> findAffiliations() {
        List<Organization> organizations = organizationLoadPort.findAll();
        return organizations.stream()
                .map(Organization::getAffiliation) // Organization에서 affiliation 필드만 추출
                .collect(Collectors.toSet()); //
    }
}
