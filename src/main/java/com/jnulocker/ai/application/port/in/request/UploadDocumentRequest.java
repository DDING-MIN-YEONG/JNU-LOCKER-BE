package com.jnulocker.ai.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadDocumentRequest(
        @Schema(description = "업로드할 파일 (PDF, TXT)", required = true)
                @NotNull(message = "파일은 필수입니다.")
                MultipartFile file,
        @Schema(description = "문서 카테고리 (선택)", example = "manual") String category) {}
