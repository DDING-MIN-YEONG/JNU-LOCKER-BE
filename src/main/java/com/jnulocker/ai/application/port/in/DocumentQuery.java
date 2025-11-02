package com.jnulocker.ai.application.port.in;

import com.jnulocker.ai.application.port.in.response.DocumentCustomPage;
import com.jnulocker.ai.application.port.in.response.DocumentDetailResponse;
import com.jnulocker.ai.domain.AiDocument;
import org.springframework.data.domain.Pageable;

public interface DocumentQuery {

    DocumentCustomPage getDocuments(Pageable pageable, String category);

    DocumentDetailResponse getDocument(Long documentId);

    AiDocument getDocumentEntity(Long documentId);
}
