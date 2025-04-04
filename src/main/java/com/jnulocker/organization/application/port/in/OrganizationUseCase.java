package com.jnulocker.organization.application.port.in;

import java.util.Set;

public interface OrganizationUseCase {
    Set<String> findAffiliations();
}
