package com.jnulocker.organization.adapter.in;

import com.jnulocker.organization.adapter.in.docs.OrganizationApi;
import com.jnulocker.organization.application.port.in.GetAffiliationQuery;
import com.jnulocker.organization.application.port.in.GetDepartmentQuery;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController implements OrganizationApi {
    private final GetAffiliationQuery getAffiliationQuery;
    private final GetDepartmentQuery getDepartmentQuery;

    @GetMapping("/affiliations")
    public ResponseEntity<Set<String>> getAffiliations() {
        Set<String> affiliations = getAffiliationQuery.getAffiliations();
        return ResponseEntity.ok(affiliations);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<String>> getDepartments(@RequestParam String affiliation) {
        List<String> departments = getDepartmentQuery.getDepartments(affiliation);
        return ResponseEntity.ok(departments);
    }
}
