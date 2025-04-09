package com.jnulocker.auth.application.service;

import com.jnulocker.auth.application.port.in.UserSignupCommand;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.exception.RoleNotCorrectException;
import com.jnulocker.auth.exception.UserAlreadyExistException;
import com.jnulocker.member.application.port.in.MemberCommand;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.organization.application.port.in.DepartmentQuery;
import com.jnulocker.organization.domain.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserSignupCommand {
    private final PasswordEncoder passwordEncoder;
    private final MemberQuery memberQuery;
    private final MemberCommand memberCommand;
    private final DepartmentQuery departmentQuery;

    @Override
    @Transactional
    public void signupUser(UserSignupRequest request) {
        if (memberQuery.existsByEmail(request.email())) {
            throw UserAlreadyExistException.EXCEPTION;
        }

        if (!request.role().equals(Role.USER)) {
            throw RoleNotCorrectException.EXCEPTION;
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Department department = departmentQuery.getDepartmentByIdOrThrow(request.departmentId());

        Member member =
                Member.createUser(
                        request.name(),
                        request.email(),
                        encodedPassword,
                        request.phoneNumber(),
                        department);

        memberCommand.save(member);
    }
}
