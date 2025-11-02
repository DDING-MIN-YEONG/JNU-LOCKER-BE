package com.jnulocker.ai.adapter.out;

import com.jnulocker.ai.domain.AiDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiDocumentJpaRepository extends JpaRepository<AiDocument, Long> {

    Page<AiDocument> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<AiDocument> findByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);
}
