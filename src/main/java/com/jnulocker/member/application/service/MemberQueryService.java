package com.jnulocker.member.application.service;

import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.application.port.out.MemberLoadPort;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
