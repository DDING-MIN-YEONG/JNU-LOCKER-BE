package com.jnulocker.auth.adapter.out;

import com.jnulocker.auth.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<RefreshToken, Long> {
    boolean existsByToken(String refreshToken);
}
