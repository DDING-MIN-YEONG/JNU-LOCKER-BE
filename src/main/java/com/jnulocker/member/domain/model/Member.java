package com.jnulocker.member.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
    private final MemberId id;
    private final MemberInfo memberInfo;
    private final AuthInfo authInfo;
    private final Role role;

    public static Member create(MemberInfo memberInfo, AuthInfo authInfo) {
        Member member =
                Member.builder()
                        .id(new MemberId())
                        .memberInfo(memberInfo)
                        .authInfo(authInfo)
                        .role(Role.USER)
                        .build();
        return member;
    }

    public static Member load(MemberId id, MemberInfo memberInfo, AuthInfo authInfo, Role role) {
        return Member.builder().id(id).memberInfo(memberInfo).authInfo(authInfo).build();
    }
}
