package com.jnulocker.ai.application.port.in.response;

public record AiChatResponse(String message) {
    public static AiChatResponse of(String message) {
        return new AiChatResponse(message);
    }
}
