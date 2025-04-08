package com.jnulocker.organization.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.organization.application.port.out.DepartmentLoadPort;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class DepartmentPersistenceAdapter implements DepartmentLoadPort {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> getDepartmentsByOrganizationId(Long organizationId) {
        return departmentRepository.findAllByOrganizationId(organizationId);
    }

    //    @Override
    //    public Department getByOrganizationAndName(Organization organization, String name) {
    //        return departmentRepository.findByOrganizationAndName(organization, name);
    //    }
    @Override
    public Department getByOrganizationAndId(Organization organization, Long id) {
        return departmentRepository.findByOrganizationAndId(organization, id);
    }
}
