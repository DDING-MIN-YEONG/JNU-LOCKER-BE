package com.jnulocker.organization.application.port.in;

import com.jnulocker.organization.application.port.in.response.OrganizationResponse;
import com.jnulocker.organization.domain.OrganizationType;
import java.util.List;

public interface OrganizationQuery {

    List<OrganizationResponse> getOrganizations(OrganizationType organizationType);
}
