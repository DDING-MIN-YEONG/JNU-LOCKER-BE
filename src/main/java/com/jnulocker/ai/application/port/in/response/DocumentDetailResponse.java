package com.jnulocker.ai.application.port.in.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.ai.domain.AiDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public record DocumentDetailResponse(
        @Schema(description = "문서 ID", example = "1") Long documentId,
        @Schema(description = "파일명", example = "user-guide.pdf") String fileName,
        @Schema(description = "원본 파일명", example = "사용자_가이드.pdf") String originalFileName,
        @Schema(description = "파일 크기 (bytes)", example = "2048576") Long fileSize,
        @Schema(description = "파일 타입", example = "PDF") String fileType,
        @Schema(description = "카테고리", example = "manual") String category,
        @Schema(description = "청크 개수", example = "15") Integer chunkCount,
        @Schema(description = "업로드 시간", example = "2025-10-12T10:30:00") LocalDateTime uploadedAt,
        @Schema(description = "업로드한 회원 ID", example = "123") Long uploadedBy,
        @Schema(description = "Vector Store ID 목록") List<String> vectorStoreIds) {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static DocumentDetailResponse from(AiDocument document) {
        List<String> vectorIds = parseVectorStoreIds(document.getVectorStoreIds());

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

    private static List<String> parseVectorStoreIds(String vectorStoreIds) {
        if (vectorStoreIds == null || vectorStoreIds.isBlank()) {
            return List.of();
        }

        try {
            return objectMapper.readValue(vectorStoreIds, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }
}
