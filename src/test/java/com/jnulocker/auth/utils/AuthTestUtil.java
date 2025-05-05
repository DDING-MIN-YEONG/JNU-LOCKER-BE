package com.jnulocker.auth.utils;

import com.jnulocker.auth.jwt.TokenProvider;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.member.utils.MemberTestUtil;
import com.jnulocker.organization.domain.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthTestUtil {

    @Autowired private MemberTestUtil memberTestUtil;

    @Autowired private TokenProvider tokenProvider;

    public String generateAccessToken(Role role) {
        Member member = memberTestUtil.createMemberFromRole(role);
        return tokenProvider.generateAccessToken(member.getId(), member.getRole());
    }

    public String generateAccessTokenWithDepartment(Role role, Department department) {
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(role, department);
        return tokenProvider.generateAccessToken(member.getId(), member.getRole());
    }

    public String generateAccessTokenWithAnotherDepartment(Role role) {
        Member member = memberTestUtil.createMemberFromRoleWithAnotherDepartment(role);
        return tokenProvider.generateAccessToken(member.getId(), member.getRole());
    }

    public String generateAccessTokenFromMemberId(Long memberId, Role role) {
        return tokenProvider.generateAccessToken(memberId, role);
    }
}
