package com.jnulocker.ai.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AiChatRequest(
        @Schema(description = "사용자 메시지", example = "안녕 AI 챗봇!")
                @NotBlank(message = "메시지는 필수 입력값입니다")
                String message) {}
