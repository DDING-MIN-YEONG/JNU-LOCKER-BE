package com.jnulocker.organization.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.organization.application.port.out.OrganizationLoadPort;
import com.jnulocker.organization.domain.Organization;
import com.jnulocker.organization.exception.OrganizationNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class OrganizationPersistenceAdapter implements OrganizationLoadPort {
    private final OrganizationRepository organizationRepository;

    @Override
    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }

    @Override
    public List<Organization> getByAffiliation(String affiliation) {
        return organizationRepository.findByAffiliation(affiliation);
    }

    @Override
    public Organization getByAffiliationAndDepartment(String affiliation, String department) {
        if (department == null) {
            return organizationRepository
                    .findByAffiliationAndDepartmentIsNull(affiliation)
                    .orElseThrow(() -> OrganizationNotFoundException.EXCEPTION);
        }
        return organizationRepository
                .findByAffiliationAndDepartment(affiliation, department)
                .orElseThrow(() -> OrganizationNotFoundException.EXCEPTION);
    }
}
