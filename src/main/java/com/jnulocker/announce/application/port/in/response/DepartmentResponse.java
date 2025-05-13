package com.jnulocker.announce.application.port.in.response;

import com.jnulocker.organization.domain.Department;
import io.swagger.v3.oas.annotations.media.Schema;

public record DepartmentResponse(
        @Schema(description = "학과 ID", example = "101") Long id,
        @Schema(description = "학과 이름", example = "컴퓨터정보통신공학과") String name) {
    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(department.getId(), department.getName());
    }
}
