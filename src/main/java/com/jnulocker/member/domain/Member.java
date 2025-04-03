package com.jnulocker.member.domain;

import com.jnulocker.common.persistence.BaseEntity;
import com.jnulocker.organization.domain.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    // 관리자 회원가입시 사용
    public static Member createManager(
            String email, String password, String phoneNumber, Organization organization) {
        return Member.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(Role.GUEST)
                .organization(organization)
                .build();
    }

    // 유저 회원가입시 사용
    public static Member createUser(
            String email, String password, String phoneNumber, Organization organization) {
        return Member.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .organization(organization)
                .build();
    }

    public Member(
            String email,
            String password,
            String phoneNumber,
            Role role,
            Organization organization) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.organization = organization;
    }

    public void acceptManager() {
        this.role = Role.MANAGER;
    }
}
