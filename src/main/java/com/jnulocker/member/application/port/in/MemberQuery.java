package com.jnulocker.member.application.port.in;

public interface MemberQuery {

    boolean existsByEmail(String email);
}
