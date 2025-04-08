package com.jnulocker.auth.application.port.in;

import com.jnulocker.auth.application.port.in.request.UserSignupRequest;

public interface UserSignupCommand {
    void signupUser(UserSignupRequest userSignupRequest);
}
