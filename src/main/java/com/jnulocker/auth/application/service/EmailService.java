package com.jnulocker.auth.application.service;

import com.jnulocker.auth.application.port.in.SendEmailCommand;
import com.jnulocker.auth.application.port.in.VerifyCodeCommand;
import com.jnulocker.auth.application.port.in.request.SendEmailRequest;
import com.jnulocker.auth.application.port.in.request.VerifyCodeRequest;
import com.jnulocker.auth.exception.CodeExpiredException;
import com.jnulocker.auth.exception.CodeNotCorrectException;
import com.jnulocker.auth.exception.SendEmailException;
import com.jnulocker.common.util.RedisUtil;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService implements SendEmailCommand, VerifyCodeCommand {
    private static final int FIVE_MINUTIES = 300;
    private static final int ONE_HOUR = 3600;
    private static final String VERIFIED_PREFIX = ":verified";

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    @Override
    public void sendEmail(SendEmailRequest request) {
        int code = createCode();
        String form = generateHtml(code);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(request.email());
            helper.setSubject("[전남대학교 사물함 신청 서비스] 인증 코드 전송");
            helper.setText(form, true);

            javaMailSender.send(message);
            redisUtil.setDataExpire(request.email(), Integer.toString(code), FIVE_MINUTIES);
        } catch (Exception e) {
            throw SendEmailException.EXCEPTION;
        }
    }

    @Override
    public void verify(VerifyCodeRequest request) {
        String storedCode = redisUtil.getData(request.email());
        if (storedCode == null) {
            throw CodeExpiredException.EXCEPTION;
        }
        if (!request.code().equals(storedCode)) {
            throw CodeNotCorrectException.EXCEPTION;
        }

        redisUtil.setDataExpire(request.email() + VERIFIED_PREFIX, "true", ONE_HOUR);
        redisUtil.deleteData(request.email());
    }

    public int createCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    public String generateHtml(int code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("email-verification", context);
    }
}
