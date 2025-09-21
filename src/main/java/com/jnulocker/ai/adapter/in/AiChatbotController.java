package com.jnulocker.ai.adapter.in;

import com.jnulocker.ai.adapter.in.docs.AiChatbotApi;
import com.jnulocker.ai.application.port.in.AiChatCommand;
import com.jnulocker.ai.application.port.in.request.AiChatRequest;
import com.jnulocker.ai.application.port.in.response.AiChatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai/chat")
public class AiChatbotController implements AiChatbotApi {

    private final AiChatCommand aiChatCommand;

    @PostMapping
    public ResponseEntity<AiChatResponse> post(@Valid @RequestBody AiChatRequest request) {
        AiChatResponse chatResponse = aiChatCommand.chat(request);
        return ResponseEntity.ok(chatResponse);
    }
}
