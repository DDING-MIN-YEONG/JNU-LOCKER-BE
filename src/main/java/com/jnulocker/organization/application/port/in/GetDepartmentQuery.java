package com.jnulocker.organization.application.port.in;

import java.util.List;

public interface GetDepartmentQuery {
    List<String> findDepartments(String affiliation);
}
