package com.jnulocker.organization.adapter.out;

import com.jnulocker.organization.application.port.out.OrganizationLoadPort;
import com.jnulocker.organization.domain.Organization;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationPersistenceAdapter implements OrganizationLoadPort {
    private final OrganizationRepository organizationRepository;

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public List<Organization> loadByAffiliation(String affiliation) {
        return organizationRepository.findByAffiliation(affiliation);
    }
}
