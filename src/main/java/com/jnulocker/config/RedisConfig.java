package com.jnulocker.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializer;

@Profile({"local", "dev", "prod"})
@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${custom.lock.watchdog.timeout:30000}")
    private long lockWatchdogTimeout;

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return RedisSerializer.json(); // 기본값은 JdkSerializationRedisSerializer
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        // Watchdog 타임아웃 설정 (기본: 30초)
        config.setLockWatchdogTimeout(lockWatchdogTimeout);

        // Redis 연결 설정
        String address = String.format("redis://%s:%d", redisHost, redisPort);
        config.useSingleServer()
                .setAddress(address)
                .setPassword(redisPassword.isEmpty() ? null : redisPassword);

        return Redisson.create(config);
    }
}
