package com.jnulocker.member.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfo {
    private final String name;
    private final String affiliation;
    private final String department;
    private final String phone;

    public static MemberInfo create(
            String name, String affiliatiion, String department, String phone) {
        return new MemberInfo(name, affiliatiion, department, name);
    }

    public static MemberInfo load(
            String name, String affiliatiion, String department, String phone) {
        return new MemberInfo(name, affiliatiion, department, name);
    }
}
