package com.jnulocker.member.application.port.out;

import com.jnulocker.member.domain.Member;
import java.util.Optional;

public interface MemberLoadPort {
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);
}
