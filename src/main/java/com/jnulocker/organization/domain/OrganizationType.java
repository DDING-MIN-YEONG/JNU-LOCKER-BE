package com.jnulocker.organization.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrganizationType {
    COUNCIL("council"),
    COMMITTEE("committee");

    private final String type;
}
