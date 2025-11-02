package com.jnulocker.ai.application.port.out;

import com.jnulocker.ai.domain.AiDocument;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentLoadPort {

    Optional<AiDocument> findById(Long id);

    Page<AiDocument> findAll(Pageable pageable);

    Page<AiDocument> findByCategory(String category, Pageable pageable);

    boolean existsById(Long id);
}
