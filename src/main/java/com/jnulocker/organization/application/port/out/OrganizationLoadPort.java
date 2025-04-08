package com.jnulocker.organization.application.port.out;

import com.jnulocker.organization.domain.Organization;
import com.jnulocker.organization.domain.OrganizationType;
import java.util.List;

public interface OrganizationLoadPort {

    List<Organization> getAllByType(OrganizationType type);

    boolean existsById(Long organizationId);

    Organization getById(Long id);
}
