package com.jnulocker.member.utils;

import static member.domain.MemberTestDataBuilder.memberBuilder;
import static organization.domain.DepartmentTestDataBuilder.departmentBuilder;
import static organization.domain.OrganizationTestDataBuilder.organizationBuilder;

import com.jnulocker.member.adapter.out.MemberRepository;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.organization.adapter.out.DepartmentRepository;
import com.jnulocker.organization.adapter.out.OrganizationRepository;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberTestUtil {

    @Autowired private OrganizationRepository organizationRepository;

    @Autowired private DepartmentRepository departmentRepository;

    @Autowired private MemberRepository memberRepository;

    public Member createMemberFromRole(Role role) {
        switch (role) {
            case USER -> {
                return createUser();
            }
            case GUEST -> {
                return createGuest();
            }
            case MANAGER -> {
                return createManager();
            }
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public Member createMemberFromRoleWithAnotherDepartment(Role role) {
        Department department = getDepartment();

        switch (role) {
            case USER -> {
                return memberRepository.save(
                        memberBuilder().withDepartment(department).buildUser());
            }
            case GUEST, MANAGER -> {
                return memberRepository.save(
                        memberBuilder().withDepartment(department).buildManager());
            }
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public Member createUser() {
        Department department = getDepartment();

        Member member = memberBuilder().withDepartment(department).buildUser();
        return memberRepository.save(member);
    }

    public Member createGuest() {
        Department department = getDepartment();

        Member member = memberBuilder().withDepartment(department).buildManager();
        return memberRepository.save(member);
    }

    public Member createManager() {
        Department department = getDepartment();

        Member member = memberBuilder().withDepartment(department).buildManager();
        member.approveManager();
        return memberRepository.save(member);
    }

    private Department getDepartment() {
        Organization organization = organizationBuilder().build();
        Organization savedOrganization = organizationRepository.save(organization);

        Department department = departmentBuilder().withOrganization(savedOrganization).build();
        return departmentRepository.save(department);
    }

    public void deleteAll() {
        memberRepository.deleteAll();
    }
}
