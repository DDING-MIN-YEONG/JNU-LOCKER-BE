package com.jnulocker.organization.adapter.out;

import com.jnulocker.organization.domain.Organization;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByAffiliation(String affiliation);
}
