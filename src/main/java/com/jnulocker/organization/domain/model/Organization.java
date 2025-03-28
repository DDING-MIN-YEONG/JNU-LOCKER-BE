package com.jnulocker.organization.domain.model;

import com.jnulocker.member.domain.model.MemberId;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Organization {
    private final OrganizationId id;
    private final String email; // 공통연락망1
    private final String phone; // 공통연락망2
    private final String affiliation;
    private final String department;
    private final String name;
    private final List<MemberId> managers;

    public static Organization create(
            String email, String phone, String affiliation, String department, String name) {
        Organization organization =
                Organization.builder()
                        .id(new OrganizationId())
                        .email(email)
                        .phone(phone)
                        .affiliation(affiliation)
                        .department(department)
                        .name(name)
                        .managers(new ArrayList<>())
                        .build();
        return organization;
    }

    public static Organization load(
            OrganizationId id,
            String email,
            String phone,
            String affiliation,
            String department,
            String name,
            List<MemberId> managers) {
        return Organization.builder()
                .id(id)
                .email(email)
                .phone(phone)
                .affiliation(affiliation)
                .department(department)
                .name(name)
                .managers(managers)
                .build();
    }
}
