package com.jnulocker.organization.adapter.in.docs;

import com.jnulocker.common.swagger.ApiExceptionExamples;
import com.jnulocker.organization.application.port.in.response.DepartmentResponse;
import com.jnulocker.organization.application.port.in.response.OrganizationResponse;
import com.jnulocker.organization.domain.OrganizationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "위원회", description = "위원회 관련 API")
public interface OrganizationApi {

    @Operation(summary = "단과대학/위원회 목록 조회", description = "단과대학/위원회 목록을 조회합니다.")
    ResponseEntity<List<OrganizationResponse>> getOrganizations(
            @RequestParam OrganizationType type);

    @ApiExceptionExamples(GetOrganizationsExceptionDocs.class)
    @Operation(summary = "소속학과 조회", description = "단과대학에 속하는 소속학과 목록을 조회합니다.")
    ResponseEntity<List<DepartmentResponse>> getDepartments(
            @PathVariable("organization-id") Long organizationId);
}
