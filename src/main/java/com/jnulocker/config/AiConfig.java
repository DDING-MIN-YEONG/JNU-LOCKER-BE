package com.jnulocker.config;

import com.jnulocker.ai.application.advisor.DetailedLoggingAdvisor;
import com.jnulocker.ai.application.tools.AnnounceTools;
import com.jnulocker.ai.application.tools.EventTools;
import com.jnulocker.ai.application.tools.MemberTools;
import com.jnulocker.ai.application.tools.OrganizationTools;
import com.jnulocker.ai.application.tools.RegistrationTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class AiConfig {
    private static final Integer TOP_K = 5;
    private static final Double SIMILARITY_THRESHOLD = 0.1;

    private final DetailedLoggingAdvisor detailedLoggingAdvisor;

    @Value("classpath:/prompts/system-template.st")
    private Resource systemTemplate;

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder builder,
            VectorStore vectorStore,
            EventTools eventTools,
            AnnounceTools announceTools,
            MemberTools memberTools,
            RegistrationTools registrationTools,
            OrganizationTools organizationTools) {
        return builder.defaultSystem(systemTemplate)
                .defaultAdvisors(buildQuestionAnswerAdvisor(vectorStore), detailedLoggingAdvisor)
                .defaultTools(
                        eventTools,
                        announceTools,
                        memberTools,
                        registrationTools,
                        organizationTools)
                .build();
    }

    private QuestionAnswerAdvisor buildQuestionAnswerAdvisor(VectorStore vectorStore) {
        return QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(
                        SearchRequest.builder()
                                .topK(TOP_K)
                                .similarityThreshold(SIMILARITY_THRESHOLD)
                                .build())
                .build();
    }
}
