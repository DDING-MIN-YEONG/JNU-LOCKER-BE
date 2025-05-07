package com.jnulocker.auth.application.service;

import com.jnulocker.auth.application.port.in.ManagerQuery;
import com.jnulocker.auth.application.port.in.response.PendingManagerCustomPage;
import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthQueryService implements ManagerQuery {

    private final MemberQuery memberQuery;

    @Override
    public PendingManagerCustomPage getPendingManagers(Pageable pageable) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdWithDepartmentOrThrow(memberId);

        Page<Member> pendingManagers =
                memberQuery.getByRoleAndDepartment(Role.GUEST, member.getDepartment(), pageable);
        return PendingManagerCustomPage.from(pendingManagers);
    }
}
