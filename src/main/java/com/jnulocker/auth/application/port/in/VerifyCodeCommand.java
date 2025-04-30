package com.jnulocker.auth.application.port.in;

import com.jnulocker.auth.application.port.in.request.VerifyCodeRequest;

public interface VerifyCodeCommand {
    void verify(VerifyCodeRequest request);
}
