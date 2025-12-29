package com.jnulocker.auth.application.service;

import com.jnulocker.auth.adapter.out.TokenRepository;
import com.jnulocker.auth.application.port.in.LoginCommand;
import com.jnulocker.auth.application.port.in.LogoutCommand;
import com.jnulocker.auth.application.port.in.ManagerSignupCommand;
import com.jnulocker.auth.application.port.in.ReissueCommand;
import com.jnulocker.auth.application.port.in.UserSignupCommand;
import com.jnulocker.auth.application.port.in.WithdrawCommand;
import com.jnulocker.auth.application.port.in.request.LoginRequest;
import com.jnulocker.auth.application.port.in.request.ManagerApproveRequest;
import com.jnulocker.auth.application.port.in.request.ManagerRejectRequest;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.application.port.in.response.AuthToken;
import com.jnulocker.auth.event.ManagerApprovedEvent;
import com.jnulocker.auth.event.ManagerRejectedEvent;
import com.jnulocker.auth.exception.UserAlreadyExistException;
import com.jnulocker.auth.jwt.TokenProvider;
import com.jnulocker.auth.jwt.exception.JwtException;
import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.common.util.RedisLockManager;
import com.jnulocker.common.util.RedisUtil;
import com.jnulocker.member.application.port.in.MemberCommand;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import com.jnulocker.organization.application.port.in.DepartmentQuery;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.registration.application.port.in.RegistrationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthCommandService
        implements UserSignupCommand,
                ManagerSignupCommand,
                LoginCommand,
                ReissueCommand,
                LogoutCommand,
                WithdrawCommand {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MemberQuery memberQuery;
    private final MemberCommand memberCommand;
    private final DepartmentQuery departmentQuery;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;
    private final TokenRepository tokenRepository;
    private final RegistrationCommand registrationCommand;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisLockManager redisLockManager;

    @Override
    public void signupUser(UserSignupRequest request) {
        redisUtil.checkEmailVerified(request.email());

        redisLockManager.lock(
                request.email(),
                () -> {
                    validateDuplicateEmail(request.email());

                    String encodedPassword = passwordEncoder.encode(request.password());

                    Department department =
                            departmentQuery.getDepartmentByIdOrThrow(request.departmentId());

                    Member member =
                            Member.createUser(
                                    request.name(),
                                    request.email(),
                                    encodedPassword,
                                    request.phoneNumber(),
                                    request.studentNumber(),
                                    department);

                    memberCommand.save(member);
                });
    }

    @Override
    public void signupManager(ManagerSignupRequest request) {
        redisUtil.checkEmailVerified(request.email());

        redisLockManager.lock(
                request.email(),
                () -> {
                    validateDuplicateEmail(request.email());

                    String encodedPassword = passwordEncoder.encode(request.password());

                    Department department =
                            departmentQuery.getDepartmentByIdOrThrow(request.departmentId());

                    Member member =
                            Member.createManager(
                                    request.name(),
                                    request.email(),
                                    encodedPassword,
                                    request.phoneNumber(),
                                    request.studentNumber(),
                                    department);

                    memberCommand.save(member);
                });
    }

    @Override
    @Transactional
    public void approveManager(ManagerApproveRequest request) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member approver = memberQuery.findByIdOrThrow(memberId); // 승인자
        Member approvee = memberQuery.findByIdOrThrow(request.memberId()); // 승인받는 사람

        approver.validateManagerApproval(approvee.getDepartment());

        approvee.approveManager();

        // refreshToken 삭제
        tokenProvider.deleteRefreshTokenById(approvee.getId());

        publishManagerApprovedEvent(approvee);
    }

    @Override
    @Transactional
    public void rejectManager(ManagerRejectRequest request) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member approver = memberQuery.findByIdOrThrow(memberId); // 승인자
        Member rejectee = memberQuery.findByIdOrThrow(request.memberId()); // 거절 받는 사람

        approver.validateManagerApproval(rejectee.getDepartment());

        deleteMember(rejectee);

        publishManagerRejectedEvent(rejectee);
    }

    @Override
    @Transactional
    public AuthToken login(LoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(), request.password()));

        Member member = memberQuery.findByEmailOrThrow(request.email());

        return tokenProvider.createAuthTokenByAuthentication(authentication, member.getRole());
    }

    @Override
    public AuthToken reissue(String refreshToken) {
        if (refreshToken == null) {
            throw JwtException.INVALID_REFRESH_TOKEN;
        }

        if (!tokenProvider.existsByRefreshToken(refreshToken)
                || !tokenProvider.validateRefreshToken(refreshToken)) {
            throw JwtException.INVALID_REFRESH_TOKEN;
        }

        Long memberId = tokenProvider.getUserIdFromRefreshToken(refreshToken);
        Member member = memberQuery.findByIdOrThrow(memberId);

        return tokenProvider.createAuthToken(memberId, member.getRole());
    }

    @Override
    public void logout() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        tokenRepository.deleteById(memberId);
    }

    public void validateDuplicateEmail(String email) {
        if (memberQuery.existsByEmail(email)) {
            throw UserAlreadyExistException.EXCEPTION;
        }
    }

    @Override
    @Transactional
    public void withdraw() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);
        deleteMember(member);
    }

    private void deleteMember(Member member) {
        // RefreshToken 삭제
        tokenProvider.deleteRefreshTokenById(member.getId());

        // 신청 기록 삭제: 사물함 available 상태로 변경
        registrationCommand.deleteAllByMember(member);

        // 회원 삭제
        memberCommand.delete(member);
    }

    private void publishManagerApprovedEvent(Member member) {
        eventPublisher.publishEvent(
                ManagerApprovedEvent.of(member.getEmail(), member.getDepartment().getName()));
    }

    private void publishManagerRejectedEvent(Member member) {
        eventPublisher.publishEvent(
                ManagerRejectedEvent.of(member.getEmail(), member.getDepartment().getName()));
    }
}
