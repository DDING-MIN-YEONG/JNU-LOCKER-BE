package com.jnulocker.member.application.service;

import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import com.jnulocker.member.application.port.out.MemberLoadPort;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
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
    @Transactional
    public MemberInfoResponse getMemberInfo() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = findByIdOrThrow(memberId);
        return MemberInfoResponse.from(member);
    }
}
