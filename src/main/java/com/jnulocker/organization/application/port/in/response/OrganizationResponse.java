package com.jnulocker.organization.application.port.in.response;

import com.jnulocker.organization.domain.Organization;

public record OrganizationResponse(Long id, String name) {

    public static OrganizationResponse from(Organization organization) {
        return new OrganizationResponse(organization.getId(), organization.getName());
    }
}
