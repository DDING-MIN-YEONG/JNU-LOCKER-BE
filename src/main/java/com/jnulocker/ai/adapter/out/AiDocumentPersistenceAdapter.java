package com.jnulocker.ai.adapter.out;

import com.jnulocker.ai.application.port.out.DocumentLoadPort;
import com.jnulocker.ai.application.port.out.DocumentRecordPort;
import com.jnulocker.ai.domain.AiDocument;
import com.jnulocker.common.annotation.PersistenceAdapter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@PersistenceAdapter
@RequiredArgsConstructor
public class AiDocumentPersistenceAdapter implements DocumentLoadPort, DocumentRecordPort {

    private final AiDocumentJpaRepository jpaRepository;

    @Override
    public Optional<AiDocument> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public AiDocument save(AiDocument document) {
        return jpaRepository.save(document);
    }

    @Override
    public Page<AiDocument> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Page<AiDocument> findByCategory(String category, Pageable pageable) {
        return jpaRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
