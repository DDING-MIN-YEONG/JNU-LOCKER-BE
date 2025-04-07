package com.jnulocker.organization.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.organization.application.port.out.OrganizationLoadPort;
import com.jnulocker.organization.domain.Organization;
import com.jnulocker.organization.domain.OrganizationType;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class OrganizationPersistenceAdapter implements OrganizationLoadPort {
    private final OrganizationRepository organizationRepository;

    @Override
    public List<Organization> getAllByType(OrganizationType type) {
        return organizationRepository.findAllByType(type);
    }

    @Override
    public boolean existsById(Long organizationId) {
        return organizationRepository.existsById(organizationId);
    }

    @Override
    public Organization getByName(String name) {
        return organizationRepository.findByName(name);
    }
}
