package com.jnulocker.member.application.port.out;

import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.organization.domain.Department;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// TODO: get 으로 시작하도록 변경
public interface MemberLoadPort {
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Optional<Member> findByIdWithDepartment(Long memberId);

    Page<Member> getByRoleAndDepartment(Role role, Department department, Pageable pageable);
}
