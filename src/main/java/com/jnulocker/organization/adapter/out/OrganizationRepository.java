package com.jnulocker.organization.adapter.out;

import com.jnulocker.organization.domain.Organization;
import com.jnulocker.organization.domain.OrganizationType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByAffiliation(String affiliation);

    Optional<Organization> findByAffiliationAndDepartment(String affiliation, String department);

    Optional<Organization> findByAffiliationAndDepartmentIsNull(String affiliation);

    List<Organization> findAllByType(OrganizationType type);
}
