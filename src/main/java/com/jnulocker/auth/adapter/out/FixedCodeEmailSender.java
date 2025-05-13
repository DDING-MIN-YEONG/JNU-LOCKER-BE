package com.jnulocker.auth.adapter.out;

import com.jnulocker.auth.application.port.out.EmailSender;
import com.jnulocker.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "custom.email-verification.fixed", havingValue = "true")
public class FixedCodeEmailSender implements EmailSender {

    private final RedisUtil redisUtil;

    @Value("${custom.email-verification.code}")
    private int fixedCode;

    @Override
    public void sendVerificationEmail(String email, int code) {
        log.info("Fixed 환경 - 이메일 전송 생략: 코드: {} \t 이메일: {}", code, email);
        redisUtil.setEmailVerificationCode(email, code);
    }

    @Override
    public int generateVerificationCode() {
        log.info("Fixed 환경 - 고정된 코드 생성: {}", fixedCode);
        return fixedCode;
    }
}
