package com.jnulocker.ai.application.service;

import com.jnulocker.ai.application.port.in.AiChatCommand;
import com.jnulocker.ai.application.port.in.request.AiChatRequest;
import com.jnulocker.ai.application.port.in.response.AiChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * 사용자용 AI 챗봇 서비스
 *
 * <p>일반 학생/사용자를 위한 AI 챗봇 기능을 제공합니다. userChatClient를 사용하여 사용자 전용 Tools와 System Prompt로 동작합니다.
 */
@Service
@RequiredArgsConstructor
public class AiChatCommandService implements AiChatCommand {

    private final ChatClient userChatClient;

    @Override
    public AiChatResponse chat(AiChatRequest request) {
        String message = userChatClient.prompt().user(request.message()).call().content();

        return AiChatResponse.of(message);
    }
}
