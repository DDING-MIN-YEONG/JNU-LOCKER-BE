package com.jnulocker.organization.application.port.in;

import java.util.List;
import java.util.Set;

public interface GetAffiliationQuery {
    Set<String> findAffiliations();

    List<String> findDepartments(String affiliation);
}
