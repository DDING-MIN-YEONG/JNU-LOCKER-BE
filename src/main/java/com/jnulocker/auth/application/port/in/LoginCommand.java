package com.jnulocker.auth.application.port.in;

import com.jnulocker.auth.application.port.in.request.LoginRequest;
import com.jnulocker.auth.application.port.in.response.AuthToken;

public interface LoginCommand {
    AuthToken login(LoginRequest request);
}
