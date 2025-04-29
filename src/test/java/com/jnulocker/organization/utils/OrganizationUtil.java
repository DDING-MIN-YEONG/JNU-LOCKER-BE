package com.jnulocker.organization.utils;

import static organization.domain.DepartmentTestDataBuilder.departmentBuilder;
import static organization.domain.OrganizationTestDataBuilder.organizationBuilder;

import com.jnulocker.organization.adapter.out.DepartmentRepository;
import com.jnulocker.organization.adapter.out.OrganizationRepository;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationUtil {

    @Autowired private OrganizationRepository organizationRepository;

    @Autowired private DepartmentRepository departmentRepository;

    public Department createDepartment() {
        Organization organization = organizationBuilder().build();
        Organization savedOrganization = organizationRepository.save(organization);
        Department department = departmentBuilder().withOrganization(savedOrganization).build();
        departmentRepository.save(department);
        return department;
    }
}
