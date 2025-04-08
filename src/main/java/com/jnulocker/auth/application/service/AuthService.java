package com.jnulocker.auth.application.service;

import com.jnulocker.auth.application.port.in.UserSignupCommand;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.application.port.out.MemberRecordPort;
import com.jnulocker.auth.exception.RoleNotCorrectException;
import com.jnulocker.auth.exception.UserAlreadyExistException;
import com.jnulocker.member.application.port.out.MemberLoadPort;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.organization.application.port.out.DepartmentLoadPort;
import com.jnulocker.organization.application.port.out.OrganizationLoadPort;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserSignupCommand {
    private final MemberLoadPort memberLoadPort;
    private final PasswordEncoder passwordEncoder;
    private final MemberRecordPort memberRecordPort;
    private final OrganizationLoadPort organizationLoadPort;
    private final DepartmentLoadPort departmentLoadPort;

    @Override
    @Transactional
    public void signupUser(UserSignupRequest userSignupRequest) {
        if (memberLoadPort.existsByEmail(userSignupRequest.email())) {
            throw UserAlreadyExistException.EXCEPTION;
        }

        if (!userSignupRequest.role().equals(Role.USER)) {
            throw RoleNotCorrectException.EXCEPTION;
        }

        String encodedPassword = passwordEncoder.encode(userSignupRequest.password());

        Department department = departmentLoadPort.getById(userSignupRequest.departmentId());
        validOrganization(userSignupRequest.organizationId(), department.getOrganization());

        Member member =
                Member.createUser(
                        userSignupRequest.email(),
                        encodedPassword,
                        userSignupRequest.phoneNumber(),
                        department);

        memberRecordPort.save(member);
    }

    public void validOrganization(Long organizationId, Organization organization) {
        Organization organization1 = organizationLoadPort.getById(organizationId);
        if (organization1 != organization) {}
    }
}
