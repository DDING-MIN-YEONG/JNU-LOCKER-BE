package com.jnulocker.auth.application.port.in;

import com.jnulocker.auth.application.port.in.request.SendEmailRequest;

public interface SendEmailCommand {
    void sendEmail(SendEmailRequest request);
}
