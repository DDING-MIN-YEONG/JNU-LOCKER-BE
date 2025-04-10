package com.jnulocker.member.application.port.in;

import com.jnulocker.member.domain.Member;

public interface MemberQuery {

    boolean existsByEmail(String email);

    Member findByEmailOrThrow(String email);
}
