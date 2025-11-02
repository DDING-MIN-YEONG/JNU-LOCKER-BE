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
        // ChatClient에 defaultAdvisors로 QuestionAnswerAdvisor가 등록되어 있어
        // 자동으로 VectorStore에서 관련 문서를 검색하고 context로 전달합니다.
        String message = client.prompt().user(request.message()).call().content();

        return AiChatResponse.of(message);
    }
}
