package com.jnulocker.organization.application.port.in;

import com.jnulocker.organization.domain.Organization;
import java.util.List;
import java.util.Set;

public interface OrganizationUseCase {
    List<Organization> findAll();

    Set<String> findAffiliations();
}
