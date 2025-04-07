package com.jnulocker.member.domain;

import com.jnulocker.common.persistence.BaseEntity;
import com.jnulocker.organization.domain.Department;
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
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    // 관리자 회원가입시 사용
    public static Member createManager(
            String email, String password, String phoneNumber, Department department) {
        return Member.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(Role.GUEST)
                .department(department)
                .build();
    }

    // 유저 회원가입시 사용
    public static Member createUser(
            String email, String password, String phoneNumber, Department department) {
        return Member.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .department(department)
                .build();
    }

    public void acceptManager() {
        if (role == Role.GUEST) {
            role = Role.MANAGER;
        }
    }
}
