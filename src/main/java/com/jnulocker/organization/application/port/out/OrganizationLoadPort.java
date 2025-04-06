package com.jnulocker.organization.application.port.out;

import com.jnulocker.organization.domain.Organization;
import java.util.List;

public interface OrganizationLoadPort {
    List<Organization> getAll();

    List<Organization> getByAffiliation(String affiliation);

    Organization getByAffiliationAndDepartment(String affiliation, String department);
}
