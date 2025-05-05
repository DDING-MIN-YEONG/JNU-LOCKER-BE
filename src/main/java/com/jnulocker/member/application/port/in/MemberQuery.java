package com.jnulocker.member.application.port.in;

import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import com.jnulocker.member.domain.Member;

public interface MemberQuery {

    boolean existsByEmail(String email);

    Member findByEmailOrThrow(String email);

    Member findByIdOrThrow(Long id);

    Member findByIdWithDepartmentOrThrow(Long memberId);

    MemberInfoResponse getMemberInfo();
}
