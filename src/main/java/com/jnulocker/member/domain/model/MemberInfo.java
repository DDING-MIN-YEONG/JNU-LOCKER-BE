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
    private final String phoneNumber;
}
