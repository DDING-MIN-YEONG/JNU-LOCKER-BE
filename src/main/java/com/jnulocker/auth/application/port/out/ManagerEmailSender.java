package com.jnulocker.auth.application.port.out;

public interface ManagerEmailSender {
    void sendManagerApprovedEmail(String emil, String department);
}
