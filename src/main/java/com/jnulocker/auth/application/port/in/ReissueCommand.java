package com.jnulocker.auth.application.port.in;

import com.jnulocker.auth.application.port.in.response.AuthToken;

public interface ReissueCommand {
    AuthToken reissue(String refreshToken);
}
