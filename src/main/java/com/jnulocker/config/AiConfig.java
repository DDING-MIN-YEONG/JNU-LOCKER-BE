package com.jnulocker.config;

import com.jnulocker.ai.application.tools.AnnounceTools;
import com.jnulocker.ai.application.tools.EventTools;
import com.jnulocker.ai.application.tools.MemberTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class AiConfig {

    @Value("classpath:/prompts/system-template.st")
    private Resource systemTemplate;

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder builder,
            VectorStore vectorStore,
            EventTools eventTools,
            AnnounceTools announceTools,
            MemberTools memberTools) {
        return builder.defaultSystem(systemTemplate)
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(
                                        SearchRequest.builder()
                                                .topK(5) // 상위 5개 유사 문서 검색
                                                .similarityThreshold(0.7) // 유사도 임계값 0.7
                                                .build())
                                .build())
                .defaultTools(eventTools, announceTools, memberTools)
                .build();
    }
}
