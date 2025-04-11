package com.jnulocker.auth.application.service;

import com.jnulocker.auth.application.port.in.LoginCommand;
import com.jnulocker.auth.application.port.in.ManagerSignupCommand;
import com.jnulocker.auth.application.port.in.ReissueCommand;
import com.jnulocker.auth.application.port.in.UserSignupCommand;
import com.jnulocker.auth.application.port.in.request.LoginRequest;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.application.port.in.response.AuthToken;
import com.jnulocker.auth.exception.UserAlreadyExistException;
import com.jnulocker.auth.jwt.TokenProvider;
import com.jnulocker.auth.jwt.exception.InvalidRefreshTokenException;
import com.jnulocker.member.application.port.in.MemberCommand;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import com.jnulocker.organization.application.port.in.DepartmentQuery;
import com.jnulocker.organization.domain.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService
        implements UserSignupCommand, ManagerSignupCommand, LoginCommand, ReissueCommand {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final MemberQuery memberQuery;
    private final MemberCommand memberCommand;
    private final DepartmentQuery departmentQuery;
    private final TokenProvider tokenProvider;

    private static final String GRANT_TYPE = "Bearer";

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

    @Override
    @Transactional
    public AuthToken login(LoginRequest request) {
        Member member = memberQuery.findByEmailOrThrow(request.email());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.createAuthTokenByAuthentication(authentication, member.getRole());
    }

    @Override
    @Transactional
    public AuthToken reissue(String refreshToken) {
        if (!tokenProvider.existsByRefreshToken(refreshToken)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        Long memberId = tokenProvider.getUserIdFromRefreshToken(refreshToken);
        Member member = memberQuery.findByIdOrThrow(memberId);

        return tokenProvider.createAuthToken(memberId, member.getRole());
    }

    private void validateDuplicateEmail(String email) {
        if (memberQuery.existsByEmail(email)) {
            throw UserAlreadyExistException.EXCEPTION;
        }
    }
}
