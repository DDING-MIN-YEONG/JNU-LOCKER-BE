package com.jnulocker.ai.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AiChatResponse(
        @Schema(description = "AI 챗봇의 응답 메시지", example = "안녕하세요! 무엇을 도와드릴까요?") String message) {
    public static AiChatResponse of(String message) {
        return new AiChatResponse(message);
    }
}
