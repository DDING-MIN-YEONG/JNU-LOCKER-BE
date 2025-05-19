package com.jnulocker.auth.application.port.out;

public interface VerificationEmailSender {
    void sendVerificationEmail(String email, String code);

    String generateVerificationCode();
}
