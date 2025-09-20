package com.jnulocker.ai.adapter.in;

import com.jnulocker.ai.adapter.in.docs.AiChatbotApi;
import com.jnulocker.ai.application.port.in.AiChatCommand;
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
    public ResponseEntity<String> post(@RequestBody String message) {
        String chat = aiChatCommand.chat(message);
        return ResponseEntity.ok(chat);
    }
}
