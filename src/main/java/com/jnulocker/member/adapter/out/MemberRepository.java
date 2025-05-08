package com.jnulocker.member.adapter.out;

import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.organization.domain.Department;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m JOIN FETCH m.department WHERE m.id = :memberId")
    Optional<Member> findByIdWithDepartment(Long memberId);

    Page<Member> findAllByRoleAndDepartment(Role role, Department department, Pageable pageable);
}
