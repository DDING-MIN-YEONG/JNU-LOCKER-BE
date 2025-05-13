package com.jnulocker.member.application.service;

import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import com.jnulocker.member.application.port.out.MemberLoadPort;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.member.exception.MemberNotFoundException;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.OrganizationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberQueryService implements MemberQuery {

    private final MemberLoadPort memberLoadPort;

    @Override
    public boolean existsByEmail(String email) {
        return memberLoadPort.existsByEmail(email);
    }

    @Override
    public Member findByEmailOrThrow(String email) {
        return memberLoadPort
                .findByEmail(email)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    @Override
    public Member findByIdOrThrow(Long id) {
        return memberLoadPort.findById(id).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    @Override
    public Member findByIdWithDepartmentOrThrow(Long id) {
        return memberLoadPort
                .findByIdWithDepartment(id)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = findByIdOrThrow(memberId);

        String studentNumber = null;
        String affiliation = null;

        Role role = member.getRole();
        OrganizationType orgType = member.getDepartment().getOrganization().getType();

        if (role == Role.USER) {
            studentNumber = member.getStudentNumber();
            affiliation = member.getDepartment().getName();
        } else if (role == Role.MANAGER) {
            if (orgType == OrganizationType.COUNCIL) {
                studentNumber = member.getStudentNumber();
            }
            affiliation = member.getDepartment().getNickname();
        }

        return MemberInfoResponse.of(
                member.getId(),
                member.getName(),
                studentNumber,
                affiliation,
                member.getPhoneNumber(),
                member.getEmail(),
                member.getRole());
    }

    public Page<Member> getByRoleAndDepartment(
            Role role, Department department, Pageable pageable) {
        return memberLoadPort.getByRoleAndDepartment(role, department, pageable);
    }
}
