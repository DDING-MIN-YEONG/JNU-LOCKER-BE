package com.jnulocker.auth.application.service;

import com.jnulocker.auth.application.port.in.ManagerSignupCommand;
import com.jnulocker.auth.application.port.in.UserSignupCommand;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.exception.UserAlreadyExistException;
import com.jnulocker.member.application.port.in.MemberCommand;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import com.jnulocker.organization.application.port.in.DepartmentQuery;
import com.jnulocker.organization.domain.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserSignupCommand, ManagerSignupCommand {
    private final PasswordEncoder passwordEncoder;
    private final MemberQuery memberQuery;
    private final MemberCommand memberCommand;
    private final DepartmentQuery departmentQuery;

    @Override
    @Transactional
    public void signupUser(UserSignupRequest request) {
        validateDuplicateEmail(request.email());

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

    @Override
    @Transactional
    public void signupManager(ManagerSignupRequest request) {
        validateDuplicateEmail(request.email());

        String encodedPassword = passwordEncoder.encode(request.password());

        Department department = departmentQuery.getDepartmentByIdOrThrow(request.departmentId());

        Member member =
                Member.createManager(
                        request.name(),
                        request.email(),
                        encodedPassword,
                        request.phoneNumber(),
                        department);

        memberCommand.save(member);
    }

    private void validateDuplicateEmail(String email) {
        if (memberQuery.existsByEmail(email)) {
            throw UserAlreadyExistException.EXCEPTION;
        }
    }
}
