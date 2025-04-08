package com.jnulocker.auth.application.port.in;

import com.jnulocker.auth.adapter.in.request.UserSignupRequest;

public interface UserSignupUseCase {
    void signupUser(UserSignupRequest userSignupReqDto);
}
