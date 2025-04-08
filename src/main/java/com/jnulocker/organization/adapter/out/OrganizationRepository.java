package com.jnulocker.organization.adapter.out;

import com.jnulocker.organization.domain.Organization;
import com.jnulocker.organization.domain.OrganizationType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findAllByType(OrganizationType type);
}
