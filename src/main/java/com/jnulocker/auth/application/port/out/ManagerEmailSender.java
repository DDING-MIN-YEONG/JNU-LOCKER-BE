package com.jnulocker.auth.application.port.out;

public interface ManagerEmailSender {
    void sendManagerApprovedEmail(String email, String department);

    void sendManagerRejectedEmail(String email, String department);
}
