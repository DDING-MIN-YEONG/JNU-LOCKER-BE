package com.jnulocker.ai.application.port.out;

import com.jnulocker.ai.domain.AiDocument;

public interface DocumentRecordPort {

    AiDocument save(AiDocument document);

    void deleteById(Long id);
}
