package com.jnulocker.member.application.port.in;

import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.organization.domain.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQuery {

    boolean existsByEmail(String email);

    Member findByEmailOrThrow(String email);

    Member findByIdOrThrow(Long id);

    Member findByIdWithDepartmentOrThrow(Long memberId);

    MemberInfoResponse getMemberInfo();

    Page<Member> getByRoleAndDepartment(Role role, Department department, Pageable pageable);
}
