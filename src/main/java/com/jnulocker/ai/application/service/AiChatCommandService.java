package com.jnulocker.ai.application.service;

import com.jnulocker.ai.application.port.in.AiChatCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiChatCommandService implements AiChatCommand {

    private final ChatClient client;

    @Override
    @Transactional
    public String chat(String message) {

        return client.prompt().user(message).call().content();
    }
}
