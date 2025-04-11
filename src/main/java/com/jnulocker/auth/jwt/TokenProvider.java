package com.jnulocker.auth.jwt;

import com.jnulocker.auth.adapter.out.TokenRepository;
import com.jnulocker.auth.application.port.in.response.AuthToken;
import com.jnulocker.auth.jwt.exception.ExpiredTokenException;
import com.jnulocker.auth.jwt.exception.InvalidAccessTokenException;
import com.jnulocker.auth.jwt.exception.InvalidRefreshTokenException;
import com.jnulocker.auth.jwt.exception.MissingTokenException;
import com.jnulocker.member.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {
    private static final String BEARER_PREFIX = "Bearer ";

    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;
    private final Long accessTokenExpireTime;
    private final Long refreshTokenExpireTime;
    private final TokenRepository tokenRepository;

    public TokenProvider(
            @Value("${custom.jwt.access-secret-key}") String accessSecretKey,
            @Value("${custom.jwt.refresh-secret-key}") String refreshSecretKey,
            @Value("${custom.jwt.access-token-expire-time}") Long accessTokenExpireTime,
            @Value("${custom.jwt.refresh-token-expire-time}") Long refreshTokenExpireTime,
            TokenRepository tokenRepository) {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes());
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
        this.tokenRepository = tokenRepository;
    }

    public AuthToken createAuthTokenByAuthentication(Authentication authentication, Role role) {
        Long userId = Long.valueOf(authentication.getName());
        String accessToken = generateAccessToken(userId, role);
        String refreshToken = generateRefreshToken(userId);

        return AuthToken.of(
                accessToken,
                refreshToken,
                BEARER_PREFIX,
                accessTokenExpireTime,
                refreshTokenExpireTime);
    }

    public AuthToken createAuthToken(Long memberId, Role role) {
        String newAccessToken = generateAccessToken(memberId, role);
        String newRefreshToken = generateRefreshToken(memberId);
        return AuthToken.of(
                newAccessToken,
                newRefreshToken,
                BEARER_PREFIX,
                accessTokenExpireTime,
                refreshTokenExpireTime);
    }

    public String generateAccessToken(Long userId, Role role) {
        Date now = new Date();
        return Jwts.builder()
                .claim("id", userId)
                .claim("role", role.getRole())
                .issuedAt(now)
                .expiration(
                        new Date(
                                now.getTime()
                                        + Duration.ofSeconds(accessTokenExpireTime).toMillis()))
                .signWith(accessSecretKey)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        String refreshToken =
                Jwts.builder()
                        .claim("id", userId)
                        .issuedAt(now)
                        .expiration(
                                new Date(
                                        now.getTime()
                                                + Duration.ofSeconds(refreshTokenExpireTime)
                                                        .toMillis()))
                        .signWith(refreshSecretKey)
                        .compact();

        RefreshToken token = new RefreshToken(userId, refreshToken, refreshTokenExpireTime);
        tokenRepository.save(token);

        return refreshToken;
    }

    // 토큰 복호화
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken, TokenType.ACCESS);
        String userId = claims.get("id").toString();
        String role = claims.get("role").toString();
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority(role));
        UserDetails principal = new User(userId, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateAccessToken(String token) {
        parseClaims(token, TokenType.ACCESS);
        return true;
    }

    public boolean validateRefreshToken(String token) { // reissue시 사용
        parseClaims(token, TokenType.REFRESH);
        return true;
    }

    public Long getUserIdFromRefreshToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken, TokenType.REFRESH);
        return claims.get("id", Long.class);
    }

    public String extractToken(String header) {
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            throw MissingTokenException.EXCEPTION;
        }
        return header.substring(BEARER_PREFIX.length());
    }

    public boolean existsByUserIdAndRefreshToken(String refreshToken) {
        return tokenRepository.existsByToken(refreshToken);
    }

    private Claims parseClaims(String token, TokenType tokenType) {
        SecretKey secretKey =
                tokenType.equals(TokenType.ACCESS) ? accessSecretKey : refreshSecretKey;
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw ExpiredTokenException.EXCEPTION;
        } catch (Exception e) {
            if (tokenType.equals(TokenType.ACCESS)) {
                throw InvalidAccessTokenException.EXCEPTION;
            }
            throw InvalidRefreshTokenException.EXCEPTION;
        }
    }
}
