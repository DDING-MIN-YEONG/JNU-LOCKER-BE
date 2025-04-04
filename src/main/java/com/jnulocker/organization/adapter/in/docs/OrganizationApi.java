package com.jnulocker.organization.adapter.in.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "위원회", description = "위원회 관련 API")
public interface OrganizationApi {
    @Operation(summary = "단과대학/위원회 목록 조회", description = "단과대학/위원회 목록을 조회합니다.")
    ResponseEntity<Set<String>> getAffiliations();

    @Operation(summary = "소속학과 조회", description = "단과대학에 속하는 소속학과 목록을 조회합니다.")
    ResponseEntity<List<String>> getDepartments(@RequestParam String affiliation);
}
