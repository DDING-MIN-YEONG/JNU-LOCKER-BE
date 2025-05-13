package com.jnulocker.auth.application.service;

import com.jnulocker.auth.application.port.in.SendEmailCommand;
import com.jnulocker.auth.application.port.in.VerifyCodeCommand;
import com.jnulocker.auth.application.port.in.request.SendEmailRequest;
import com.jnulocker.auth.application.port.in.request.VerifyCodeRequest;
import com.jnulocker.auth.application.port.out.EmailSender;
import com.jnulocker.auth.exception.CodeNotCorrectException;
import com.jnulocker.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements SendEmailCommand, VerifyCodeCommand {

    private final RedisUtil redisUtil;
    private final EmailSender emailSender;
    private final AuthCommandService authCommandService;

    @Override
    public void sendEmail(SendEmailRequest request) {
        authCommandService.validateDuplicateEmail(request.email());

        // 기존 이메일 인증 완료 기록이 있으면 삭제
        if (redisUtil.existsVerifiedEmail(request.email())) {
            redisUtil.deleteVerifiedData(request.email());
        }

        String code = emailSender.generateVerificationCode();
        emailSender.sendVerificationEmail(request.email(), code);
    }

    @Override
    public void verify(VerifyCodeRequest request) {
        String storedCode = redisUtil.getVerificationCode(request.email());
        if (!request.code().equals(storedCode)) {
            throw CodeNotCorrectException.EXCEPTION;
        }

        redisUtil.setEmailVerified(request.email());
        redisUtil.deleteData(request.email());
    }
}
