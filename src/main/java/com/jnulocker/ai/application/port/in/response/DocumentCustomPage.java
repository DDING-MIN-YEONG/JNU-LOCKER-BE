package com.jnulocker.ai.application.port.in.response;

import com.jnulocker.ai.domain.AiDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

public record DocumentCustomPage(
        @Schema(description = "문서 목록") List<DocumentListItem> content,
        @Schema(description = "문서 전체 개수", example = "25") Long totalElements,
        @Schema(description = "마지막 페이지 여부", example = "false") Boolean last) {

    public static DocumentCustomPage from(Page<AiDocument> documents) {
        return new DocumentCustomPage(
                documents.getContent().stream().map(DocumentListItem::from).toList(),
                documents.getTotalElements(),
                documents.isLast());
    }
}
