package com.jnulocker.config;

import com.jnulocker.ai.application.advisor.DetailedLoggingAdvisor;
import com.jnulocker.ai.application.tools.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * ChatClient 설정 클래스
 *
 * <p>사용자용(userChatClient)과 관리자용(adminChatClient) ChatClient를 분리 <br>
 * 각 ChatClient는 서로 다른 System Prompt 적용
 */
@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {
    private final DetailedLoggingAdvisor detailedLoggingAdvisor;
    private final ChatModel chatModel;

    @Value("classpath:/prompts/user-system-template.st")
    private Resource userSystemTemplate;

    @Value("classpath:/prompts/admin-system-template.st")
    private Resource adminSystemTemplate;

    @Value("${spring.ai.vectorstore.top-k}")
    private Integer topK;

    @Value("${spring.ai.vectorstore.similarity-threshold}")
    private Double similarityThreshold;

    /**
     * 사용자용 ChatClient Bean
     *
     * <p>일반 사용자를 위한 ChatClient 본인의 정보 조회, 참여 가능한 이벤트 조회, 사물함 신청/취소 등 사용자 전용 기능
     *
     * @param vectorStore VectorStore (RAG용)
     * @param eventTools 사용자용 이벤트 조회 Tools
     * @param announceTools 사용자용 공지사항 조회 Tools
     * @param memberTools 회원 정보 조회 Tools
     * @param registrationTools 사물함 신청 관리 Tools (조회, 신청, 취소)
     * @param organizationTools 조직/학과 조회 Tools
     * @return 사용자용 ChatClient
     */
    @Bean
    public ChatClient userChatClient(
            VectorStore vectorStore,
            EventTools eventTools,
            AnnounceTools announceTools,
            MemberTools memberTools,
            RegistrationTools registrationTools,
            OrganizationTools organizationTools) {
        return ChatClient.builder(chatModel)
                .defaultSystem(userSystemTemplate)
                .defaultAdvisors(buildQuestionAnswerAdvisor(vectorStore), detailedLoggingAdvisor)
                .defaultTools(
                        eventTools,
                        announceTools,
                        memberTools,
                        registrationTools,
                        organizationTools)
                .build();
    }

    /**
     * 관리자용 ChatClient Bean
     *
     * <p>위원회, 관리자를 위한 ChatClient 전체 이벤트 조회, 전체 신청 현황 조회 등 관리자 전용 기능
     *
     * @param vectorStore VectorStore (RAG용)
     * @param eventManagerTools 관리자 전용 이벤트 조회 Tools
     * @param registrationManagerTools 관리자 전용 신청 현황 조회 Tools
     * @param eventTools 일반 이벤트 조회 Tools (관리자도 사용 가능)
     * @param announceTools 공지사항 조회 Tools (관리자도 사용 가능)
     * @param memberTools 회원 정보 조회 Tools
     * @param organizationTools 조직/학과 조회 Tools
     * @return 관리자용 ChatClient
     */
    @Bean
    public ChatClient adminChatClient(
            VectorStore vectorStore,
            EventManagerTools eventManagerTools,
            RegistrationManagerTools registrationManagerTools,
            EventTools eventTools,
            AnnounceTools announceTools,
            MemberTools memberTools,
            OrganizationTools organizationTools) {
        return ChatClient.builder(chatModel)
                .defaultSystem(adminSystemTemplate)
                .defaultAdvisors(buildQuestionAnswerAdvisor(vectorStore), detailedLoggingAdvisor)
                .defaultTools(
                        eventManagerTools,
                        registrationManagerTools,
                        eventTools,
                        announceTools,
                        memberTools,
                        organizationTools)
                .build();
    }

    /**
     * QuestionAnswerAdvisor 빌더
     *
     * <p>RAG (Retrieval-Augmented Generation)를 위한 Advisor를 생성합니다. VectorStore에서 유사한 문서를 검색하여 컨텍스트에
     * 포함
     *
     * @param vectorStore VectorStore
     * @return QuestionAnswerAdvisor
     */
    private QuestionAnswerAdvisor buildQuestionAnswerAdvisor(VectorStore vectorStore) {
        return QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(
                        SearchRequest.builder()
                                .topK(topK)
                                .similarityThreshold(similarityThreshold)
                                .build())
                .build();
    }
}
