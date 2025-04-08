package com.jnulocker.member.application.port.in;

import com.jnulocker.member.domain.Member;

public interface MemberCommand {
    void save(Member member);
}
