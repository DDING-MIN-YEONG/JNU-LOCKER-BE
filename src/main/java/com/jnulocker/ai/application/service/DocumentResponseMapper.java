package com.jnulocker.ai.application.service;

import com.jnulocker.ai.application.port.in.response.DocumentDetailResponse;
import com.jnulocker.ai.domain.AiDocument;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentResponseMapper {

    private final VectorStoreIdSerializer vectorStoreIdSerializer;

    public DocumentDetailResponse toDetailResponse(AiDocument document) {
        List<String> vectorIds =
                vectorStoreIdSerializer.parseVectorStoreIds(document.getVectorStoreIds());

        return new DocumentDetailResponse(
                document.getId(),
                document.getFileName(),
                document.getOriginalFileName(),
                document.getFileSize(),
                document.getFileType(),
                document.getCategory(),
                document.getChunkCount(),
                document.getCreatedAt(),
                document.getUploadedBy().getId(),
                vectorIds);
    }
}
