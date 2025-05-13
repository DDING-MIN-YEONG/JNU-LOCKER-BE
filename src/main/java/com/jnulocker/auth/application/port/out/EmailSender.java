package com.jnulocker.auth.application.port.out;

public interface EmailSender {
    void sendVerificationEmail(String email, String code);

    String generateVerificationCode();
}
