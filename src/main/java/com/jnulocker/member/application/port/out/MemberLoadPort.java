package com.jnulocker.member.application.port.out;

public interface MemberLoadPort {
    boolean existsByEmail(String email);
}
