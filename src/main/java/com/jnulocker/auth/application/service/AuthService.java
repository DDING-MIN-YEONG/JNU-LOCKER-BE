package com.jnulocker.auth.application.service;

import com.jnulocker.auth.adapter.in.request.UserSignupReqDto;
import com.jnulocker.auth.application.port.in.UserSignupUseCase;
import com.jnulocker.auth.application.port.out.UserRecordPort;
import com.jnulocker.auth.exception.RoleNotCorrectException;
import com.jnulocker.auth.exception.UserAlreadyExistException;
import com.jnulocker.member.application.port.out.MemberLoadPort;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.organization.application.port.out.OrganizationLoadPort;
import com.jnulocker.organization.domain.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserSignupUseCase {
    private final MemberLoadPort memberLoadPort;
    private final PasswordEncoder passwordEncoder;
    private final UserRecordPort userRecordPort;
    private final OrganizationLoadPort organizationLoadPort;

    @Override
    @Transactional
    public void signupUser(UserSignupReqDto userSignupReqDto) {
        // 이미 존재하는 회원이면 예외 던지기
        if (memberLoadPort.existsByEmail(userSignupReqDto.email())) {
            throw UserAlreadyExistException.EXCEPTION;
        }

        // role이 USER인지
        if (!userSignupReqDto.role().equals(Role.USER)) {
            throw RoleNotCorrectException.EXCEPTION;
        }

        // 비밀번호 복호화
        String encodedPassword = passwordEncoder.encode(userSignupReqDto.password());

        // Organization 불려오기
        Organization organization =
                organizationLoadPort.getByAffiliationAndDepartment(
                        userSignupReqDto.affiliation(), userSignupReqDto.department());

        // save
        Member member =
                Member.createUser(
                        userSignupReqDto.email(),
                        encodedPassword,
                        userSignupReqDto.phoneNumber(),
                        organization);
        userRecordPort.save(member);
    }
}
