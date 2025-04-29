package com.jnulocker.member.domain;

import com.jnulocker.auth.exception.StudentNumberRequiredException;
import com.jnulocker.common.persistence.BaseEntity;
import com.jnulocker.member.exception.OnlyGuestCanBeManagerException;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.OrganizationType;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column private String studentNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // 관리자 회원가입시 사용
    public static Member createManager(
            String name,
            String email,
            String password,
            String phoneNumber,
            String studentNumber,
            Department department) {
        if (department.getOrganization().getType() == OrganizationType.COUNCIL
                && studentNumber == null) {
            throw StudentNumberRequiredException.EXCEPTION;
        }

        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(Role.GUEST)
                .department(department)
                .build();
    }

    // 유저 회원가입시 사용
    public static Member createUser(
            String name,
            String email,
            String password,
            String phoneNumber,
            String studentNumber,
            Department department) {
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .studentNumber(studentNumber)
                .role(Role.USER)
                .department(department)
                .build();
    }

    public void approveManager() {
        if (role != Role.GUEST) {
            throw OnlyGuestCanBeManagerException.EXCEPTION;
        }
        role = Role.MANAGER;
    }
}
