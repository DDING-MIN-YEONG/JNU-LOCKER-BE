package com.jnulocker.common.util;

import com.jnulocker.auth.adapter.out.EmailVerification;
import com.jnulocker.auth.adapter.out.EmailVerificationRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private static final String VERIFIED_PREFIX = ":verified";
    private static final long EMAIL_CODE_EXPIRE = Duration.ofMinutes(5).getSeconds();
    private static final long EMAIL_VERIFIED_EXPIRE = Duration.ofHours(1).getSeconds();
    private static final String TRUE = Boolean.TRUE.toString();

    private final EmailVerificationRepository emailVerificationRepository;

    public String getData(String email) {
        return emailVerificationRepository
                .findById(email)
                .map(EmailVerification::getCode)
                .orElse(null);
    }

    private void setData(String email, String value, long expirationSeconds) {
        EmailVerification verification =
                EmailVerification.builder()
                        .email(email)
                        .code(value)
                        .expiration(expirationSeconds)
                        .build();
        emailVerificationRepository.save(verification);
    }

    public void deleteData(String email) {
        emailVerificationRepository.deleteById(email);
    }

    public void deleteVerifiedData(String email) {
        deleteData(getVerifiedKey(email));
    }

    public boolean isVerified(String email) {
        return TRUE.equals(getData(getVerifiedKey(email)));
    }

    public void setEmailVerificationCode(String email, int code) {
        setData(email, Integer.toString(code), EMAIL_CODE_EXPIRE);
    }

    public void setEmailVerified(String email) {
        setData(getVerifiedKey(email), TRUE, EMAIL_VERIFIED_EXPIRE);
    }

    public void deleteAll() {
        emailVerificationRepository.deleteAll();
    }

    private String getVerifiedKey(String email) {
        return email + VERIFIED_PREFIX;
    }
}
