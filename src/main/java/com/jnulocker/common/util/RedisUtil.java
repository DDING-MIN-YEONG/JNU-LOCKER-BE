package com.jnulocker.common.util;

import com.jnulocker.auth.adapter.out.EmailVerification;
import com.jnulocker.auth.adapter.out.EmailVerificationRepository;
import com.jnulocker.auth.exception.CodeNotFoundException;
import com.jnulocker.auth.exception.EmailNotVerifiedException;
import java.time.Duration;
import java.util.Optional;
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

    private Optional<String> getData(String key) {
        return emailVerificationRepository.findById(key).map(EmailVerification::getCode);
    }

    public String getVerificationCode(String email) {
        return getData(email).orElseThrow(() -> CodeNotFoundException.EXCEPTION);
    }

    public String getVerifiedStatus(String email) {
        return getData(email + VERIFIED_PREFIX)
                .orElseThrow(() -> EmailNotVerifiedException.EXCEPTION);
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

    public void validVerified(String email) {
        if (!TRUE.equals(getVerifiedStatus(email))) {
            throw EmailNotVerifiedException.EXCEPTION;
        }
    }

    public boolean existsEmailVerified(String email) {
        return emailVerificationRepository.findById(getVerifiedKey(email)).isPresent();
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
