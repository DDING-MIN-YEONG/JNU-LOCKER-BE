package com.jnulocker.organization.application.port.in.response;

import com.jnulocker.organization.domain.Department;
import io.swagger.v3.oas.annotations.media.Schema;

public record DepartmentResponse(
        @Schema(description = "학과 ID", example = "99") Long id,
        @Schema(description = "소속 단과대학 ID", example = "11") Long organizationId,
        @Schema(description = "학과 이름", example = "전자컴퓨터공학부") String name) {

    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(
                department.getId(), department.getOrganization().getId(), department.getName());
    }
}
