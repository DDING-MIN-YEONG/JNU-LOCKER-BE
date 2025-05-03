package com.jnulocker.auth.application.port.in;

import com.jnulocker.auth.application.port.in.request.ManagerApproveRequest;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;

public interface ManagerSignupCommand {
    void signupManager(ManagerSignupRequest request);

    void approveManager(ManagerApproveRequest request);
}
