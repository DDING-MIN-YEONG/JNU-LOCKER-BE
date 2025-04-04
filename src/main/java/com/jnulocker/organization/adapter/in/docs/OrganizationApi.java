package com.jnulocker.organization.adapter.in.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Organization", description = "Organization 관련 API")
public interface OrganizationApi {
    @Operation(summary = "소속대학 조회", description = "소속대학 목록을 조회합니다.")
    ResponseEntity<Set<String>> getAffiliations();

    @Operation(summary = "소속학과 조회", description = "소속학과 목록을 조회합니다.")
    ResponseEntity<List<String>> getDepartment(@RequestParam String affiliation);
}
