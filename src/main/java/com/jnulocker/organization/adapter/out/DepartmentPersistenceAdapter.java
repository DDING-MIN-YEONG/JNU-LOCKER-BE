package com.jnulocker.organization.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.organization.application.port.out.DepartmentLoadPort;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.exception.OrganizationNotFoundException;
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

    @Override
    public Department getById(Long id) {
        return departmentRepository
                .findById(id)
                .orElseThrow(() -> OrganizationNotFoundException.EXCEPTION);
    }
}
