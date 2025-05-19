package com.jnulocker.auth.adapter.out;

import com.jnulocker.auth.application.port.out.VerificationEmailSender;
import com.jnulocker.auth.exception.FailedToCreateCodeException;
import com.jnulocker.auth.exception.SendEmailException;
import com.jnulocker.common.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "custom.email-verification.fixed", havingValue = "false")
public class RandomCodeVerificationEmailSender implements VerificationEmailSender {

    private final RedisUtil redisUtil;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendVerificationEmail(String email, String code) {
        try {
            MimeMessage message = setupEmailForm(email, code);
            javaMailSender.send(message);
            redisUtil.setEmailVerificationCode(email, code);
        } catch (Exception e) {
            log.error("Error sending verification email failed", e);
            throw SendEmailException.EXCEPTION;
        }
    }

    private MimeMessage setupEmailForm(String email, String code) throws MessagingException {
        String form = generateEmailForm(code);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setTo(email);
        helper.setSubject("[전남대학교 사물함 신청 서비스] 인증 코드 전송");
        helper.setText(form, true);
        return message;
    }

    private String generateEmailForm(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("email-verification", context);
    }

    @Override
    public String generateVerificationCode() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            int integerCode = 100000 + random.nextInt(900000);
            return String.valueOf(integerCode);
        } catch (Exception e) {
            log.error("Error generating verification code", e);
            throw FailedToCreateCodeException.EXCEPTION;
        }
    }
}
