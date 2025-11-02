package com.jnulocker.ai.application.service;

import com.jnulocker.ai.application.port.in.DocumentQuery;
import com.jnulocker.ai.application.port.in.response.DocumentCustomPage;
import com.jnulocker.ai.application.port.in.response.DocumentDetailResponse;
import com.jnulocker.ai.application.port.out.DocumentLoadPort;
import com.jnulocker.ai.domain.AiDocument;
import com.jnulocker.ai.exception.DocumentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentQueryService implements DocumentQuery {

    private final DocumentLoadPort documentLoadPort;
    private final DocumentResponseMapper documentResponseMapper;

    @Override
    public DocumentCustomPage getDocuments(Pageable pageable, String category) {
        Page<AiDocument> documents;

        if (category != null && !category.isBlank()) {
            documents = documentLoadPort.findByCategory(category, pageable);
        } else {
            documents = documentLoadPort.findAll(pageable);
        }

        return DocumentCustomPage.from(documents);
    }

    @Override
    public DocumentDetailResponse getDocument(Long documentId) {
        AiDocument document = getDocumentEntity(documentId);
        return documentResponseMapper.toDetailResponse(document);
    }

    @Override
    public AiDocument getDocumentEntity(Long documentId) {
        return documentLoadPort
                .findById(documentId)
                .orElseThrow(() -> DocumentNotFoundException.EXCEPTION);
    }
}
