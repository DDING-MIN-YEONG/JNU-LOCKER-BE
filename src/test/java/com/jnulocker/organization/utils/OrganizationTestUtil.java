package com.jnulocker.organization.utils;

import static organization.domain.DepartmentTestDataBuilder.departmentBuilder;
import static organization.domain.OrganizationTestDataBuilder.organizationBuilder;

import com.jnulocker.organization.adapter.out.DepartmentRepository;
import com.jnulocker.organization.adapter.out.OrganizationRepository;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import com.jnulocker.organization.domain.OrganizationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationTestUtil {

    @Autowired private OrganizationRepository organizationRepository;

    @Autowired private DepartmentRepository departmentRepository;

    public Department createCouncilDepartment() {
        Organization organization = createCouncil();
        Department department = departmentBuilder().withOrganization(organization).build();
        departmentRepository.save(department);
        return department;
    }

    public Organization createCouncil() {
        Organization organization = organizationBuilder().build();
        return organizationRepository.save(organization);
    }

    public Department createCommitteeDepartment() {
        Organization organization = createCommittee();
        Department department = departmentBuilder().withOrganization(organization).build();
        departmentRepository.save(department);
        return department;
    }

    public Organization createCommittee() {
        Organization organization =
                organizationBuilder().withType(OrganizationType.COMMITTEE).build();
        return organizationRepository.save(organization);
    }
}
