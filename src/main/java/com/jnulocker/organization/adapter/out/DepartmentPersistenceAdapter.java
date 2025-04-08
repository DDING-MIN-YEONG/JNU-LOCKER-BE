package com.jnulocker.organization.adapter.out;

import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.organization.application.port.out.DepartmentLoadPort;
import com.jnulocker.organization.domain.Department;
import java.util.List;
import java.util.Optional;
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
    public Optional<Department> getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId);
    }
}
