package com.jnulocker.organization.adapter.in;

import com.jnulocker.organization.application.port.in.OrganizationUseCase;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationUseCase organizationUseCase;

    @GetMapping("/affiliations")
    public ResponseEntity<Set<String>> getAffiliations() {
        Set<String> affiliations = organizationUseCase.findAffiliations();
        return new ResponseEntity<>(affiliations, HttpStatus.OK);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<String>> getDepartment(@RequestParam String affiliation) {
        List<String> departments = organizationUseCase.findDepartments(affiliation);
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }
}
