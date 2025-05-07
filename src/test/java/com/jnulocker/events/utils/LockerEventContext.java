package com.jnulocker.events.utils;

import com.jnulocker.events.domain.Event;
import com.jnulocker.member.domain.Member;
import com.jnulocker.organization.domain.Department;

public record LockerEventContext(
        Department department, Member member, String accessToken, Event event) {}
