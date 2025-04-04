package com.jnulocker.organization.application.port.out;

import com.jnulocker.organization.domain.Organization;
import java.util.List;

public interface OrganizationLoadPort {
    List<Organization> findAll();

    List<Organization> loadByAffiliation(String affiliation);
}
