package com.jnulocker.common.util;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private static final String VERIFIED_PREFIX = ":verified";
    private static final Duration EMAIL_CODE_EXPIRE = Duration.ofMinutes(5);
    private static final Duration EMAIL_VERIFIED_EXPIRE = Duration.ofHours(1);
    private static final String TRUE = Boolean.TRUE.toString();

    private final StringRedisTemplate redisTemplate;

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setData(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public boolean isVerified(String email) {
        return TRUE.equals(getData(getVerifiedKey(email)));
    }

    public void setEmailVerificationCode(String email, int code) {
        setData(email, Integer.toString(code), EMAIL_CODE_EXPIRE);
    }

    public void setEmailVerified(String email) {
        setData(getVerifiedKey(email), TRUE, EMAIL_VERIFIED_EXPIRE);
    }

    private String getVerifiedKey(String email) {
        return email + VERIFIED_PREFIX;
    }

    public void deleteAll() {
        deleteData("*");
    }
}
