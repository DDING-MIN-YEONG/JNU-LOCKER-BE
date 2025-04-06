package com.jnulocker.auth.application.port.in;

import com.jnulocker.auth.adapter.in.request.UserSignupReqDto;

public interface UserSignupUseCase {
    void signupUser(UserSignupReqDto userSignupReqDto);
}
