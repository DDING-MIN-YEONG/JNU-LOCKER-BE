package com.jnulocker.member.application.service;

import com.jnulocker.auth.application.port.out.MemberRecordPort;
import com.jnulocker.member.application.port.in.MemberCommand;
import com.jnulocker.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCommandService implements MemberCommand {
    private final MemberRecordPort memberRecordPort;

    @Override
    @Transactional
    public void save(Member member) {
        memberRecordPort.save(member);
    }
}
