package com.jnulocker.auth.application.port.out;

import com.jnulocker.member.domain.Member;

public interface MemberRecordPort {
    void save(Member member);
}
