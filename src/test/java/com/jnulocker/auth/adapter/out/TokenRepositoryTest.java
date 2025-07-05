package com.jnulocker.auth.adapter.out;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.jnulocker.auth.jwt.RefreshToken;
import com.jnulocker.config.RedisTest;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TokenRepositoryTest extends RedisTest {

    @Autowired TokenRepository tokenRepository;

    @AfterEach
    void tearDown() {
        tokenRepository.deleteAll();
    }

    @Test
    void 레포지토리_생성() {
        assertNotNull(tokenRepository);
    }

    @Test
    void 리프레시토큰_저장() {
        // given
        String value = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken(1L, value, 10L);

        // when
        RefreshToken savedToken = tokenRepository.save(token);

        // then
        assertEquals(token, savedToken);
    }

    @Test
    void 리프레시토큰_조회() {
        // given
        String value = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken(1L, value, 10L);

        // when
        tokenRepository.save(token);
        RefreshToken foundToken = tokenRepository.findById(1L).orElse(null);

        boolean exists = tokenRepository.existsByToken(value);

        // then
        assertThat(token.getToken()).isEqualTo(foundToken.getToken());
        assertThat(exists).isTrue();
    }

    @Test
    void 리프레시토큰_삭제() {
        // given
        String value = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken(1L, value, 10L);

        // when
        tokenRepository.save(token);
        tokenRepository.delete(token);

        // then
        assertFalse(tokenRepository.findById(1L).isPresent());
    }
}
