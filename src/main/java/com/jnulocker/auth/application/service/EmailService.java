package com.jnulocker.auth.application.service;

import com.jnulocker.auth.application.port.in.SendEmailCommand;
import com.jnulocker.auth.application.port.in.VerifyCodeCommand;
import com.jnulocker.auth.application.port.in.request.SendEmailRequest;
import com.jnulocker.auth.application.port.in.request.VerifyCodeRequest;
import com.jnulocker.auth.exception.CodeNotCorrectException;
import com.jnulocker.auth.exception.FailedToCreateCodeException;
import com.jnulocker.auth.exception.SendEmailException;
import com.jnulocker.common.util.RedisUtil;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements SendEmailCommand, VerifyCodeCommand {
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final AuthService authService;

    @Override
    public void sendEmail(SendEmailRequest request) {
        authService.validateDuplicateEmail(request.email());

        // 기존 이메일 인증 완료 기록이 있으면 삭제
        if (redisUtil.existsVerifiedEmail(request.email())) {
            redisUtil.deleteVerifiedData(request.email());
        }

        int code = createCode();
        String form = generateHtml(code);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(request.email());
            helper.setSubject("[전남대학교 사물함 신청 서비스] 인증 코드 전송");
            helper.setText(form, true);

            javaMailSender.send(message);
            redisUtil.setEmailVerificationCode(request.email(), code);
        } catch (Exception e) {
            throw SendEmailException.EXCEPTION;
        }
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

    private int createCode() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            return 100000 + random.nextInt(900000);
        } catch (Exception e) {
            log.error("Error generating verification code", e);
            throw FailedToCreateCodeException.EXCEPTION;
        }
    }

    private String generateHtml(int code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("email-verification", context);
    }
}
