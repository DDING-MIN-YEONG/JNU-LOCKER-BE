package com.jnulocker.ai.application.service;

import com.jnulocker.ai.application.port.in.AiChatCommand;
import com.jnulocker.ai.application.port.in.request.AiChatRequest;
import com.jnulocker.ai.application.port.in.response.AiChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChatCommandService implements AiChatCommand {

    private final ChatClient client;

    @Override
    public AiChatResponse chat(AiChatRequest request) {
        String message = client.prompt().user(request.message()).call().content();

        return AiChatResponse.of(message);
    }
}
