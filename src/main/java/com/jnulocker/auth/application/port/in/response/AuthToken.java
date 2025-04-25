package com.jnulocker.auth.application.port.in.response;

public record AuthToken(
        String accessToken,
        String refreshToken,
        String grantType,
        Long accessTokenExpiresIn,
        Long refreshTokenExpiresIn) {
    public static AuthToken of(
            String accessToken,
            String refreshToken,
            String grantType,
            Long accessTokenExpiresIn,
            Long refreshTokenExpiresIn) {
        return new AuthToken(
                accessToken, refreshToken, grantType, accessTokenExpiresIn, refreshTokenExpiresIn);
    }
}
