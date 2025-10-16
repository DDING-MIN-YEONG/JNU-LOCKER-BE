package com.jnulocker.ai.application.port.in.response;

import com.jnulocker.ai.domain.AiDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record DocumentListItem(
        @Schema(description = "문서 ID", example = "1") Long documentId,
        @Schema(description = "파일명", example = "user-guide.pdf") String fileName,
        @Schema(description = "파일 크기 (bytes)", example = "2048576") Long fileSize,
        @Schema(description = "카테고리", example = "manual") String category,
        @Schema(description = "청크 개수", example = "15") Integer chunkCount,
        @Schema(description = "업로드 시간", example = "2025-10-12T10:30:00") LocalDateTime uploadedAt) {

    public static DocumentListItem from(AiDocument document) {
        return new DocumentListItem(
                document.getId(),
                document.getFileName(),
                document.getFileSize(),
                document.getCategory(),
                document.getChunkCount(),
                document.getCreatedAt());
    }
}
