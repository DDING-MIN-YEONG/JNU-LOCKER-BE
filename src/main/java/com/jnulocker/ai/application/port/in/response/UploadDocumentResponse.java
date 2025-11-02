package com.jnulocker.ai.application.port.in.response;

import com.jnulocker.ai.domain.AiDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record UploadDocumentResponse(
        @Schema(description = "문서 ID", example = "1") Long documentId,
        @Schema(description = "파일명", example = "user-guide.pdf") String fileName,
        @Schema(description = "청크 개수", example = "15") Integer chunkCount,
        @Schema(description = "업로드 시간", example = "2025-10-12T10:30:00") LocalDateTime uploadedAt) {

    public static UploadDocumentResponse from(AiDocument document) {
        return new UploadDocumentResponse(
                document.getId(),
                document.getFileName(),
                document.getChunkCount(),
                document.getCreatedAt());
    }
}
