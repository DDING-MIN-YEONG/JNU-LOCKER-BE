package com.jnulocker.organization.application.port.in.response;

import com.jnulocker.organization.domain.Organization;
import io.swagger.v3.oas.annotations.media.Schema;

public record OrganizationResponse(
        @Schema(description = "조직 ID", example = "11") Long id,
        @Schema(description = "조직 이름", example = "공과대학") String name) {

    public static OrganizationResponse from(Organization organization) {
        return new OrganizationResponse(organization.getId(), organization.getName());
    }
}
