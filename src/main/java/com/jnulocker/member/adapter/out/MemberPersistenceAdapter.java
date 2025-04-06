package com.jnulocker.member.adapter.out;

import com.jnulocker.auth.application.port.out.UserRecordPort;
import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.member.application.port.out.MemberLoadPort;
import com.jnulocker.member.domain.Member;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberLoadPort, UserRecordPort {
    private final MemberRepository memberRepository;

    @Override
    public boolean existByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }
}
