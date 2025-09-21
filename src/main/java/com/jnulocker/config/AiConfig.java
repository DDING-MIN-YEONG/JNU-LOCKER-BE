package com.jnulocker.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class AiConfig {

    @Value("classpath:/prompts/system-template.st")
    private Resource systemTemplate;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(systemTemplate).build();
    }
}
