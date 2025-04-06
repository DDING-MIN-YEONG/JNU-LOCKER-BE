package com.jnulocker.member.application.port.out;

public interface MemberLoadPort {
    boolean existByEmail(String email);
}
