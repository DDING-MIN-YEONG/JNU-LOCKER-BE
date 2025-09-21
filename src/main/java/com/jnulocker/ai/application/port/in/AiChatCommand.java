package com.jnulocker.ai.application.port.in;

import com.jnulocker.ai.application.port.in.request.AiChatRequest;
import com.jnulocker.ai.application.port.in.response.AiChatResponse;

public interface AiChatCommand {
    AiChatResponse chat(AiChatRequest request);
}
