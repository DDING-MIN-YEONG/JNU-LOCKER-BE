package com.jnulocker.member.adapter.out;

import com.jnulocker.auth.application.port.out.MemberRecordPort;
import com.jnulocker.common.annotation.PersistenceAdapter;
import com.jnulocker.member.application.port.out.MemberLoadPort;
import com.jnulocker.member.domain.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberLoadPort, MemberRecordPort {
    private final MemberRepository memberRepository;

    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
