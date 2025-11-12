package com.jnulocker.ai.application.advisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.document.Document;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DetailedLoggingAdvisor implements CallAdvisor, StreamAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(DetailedLoggingAdvisor.class);
    private final ObjectMapper objectMapper;

    public DetailedLoggingAdvisor() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public String getName() {
        return "DetailedLoggingAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        logRequest(request);
        ChatClientResponse response = chain.nextCall(request);
        logResponse(response);
        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(
            ChatClientRequest request, StreamAdvisorChain chain) {
        logRequest(request);
        return chain.nextStream(request)
                .doOnComplete(
                        () ->
                                logger.debug(
                                        "Stream completed for request: {}",
                                        request.prompt().getUserMessage()));
    }

    private void logRequest(ChatClientRequest request) {
        try {
            Map<String, Object> requestInfo = new HashMap<>();

            // Messages 정보 (System prompt, User prompt 등)
            List<Map<String, Object>> messages =
                    request.prompt().getInstructions().stream().map(this::formatMessage).toList();

            requestInfo.put("messages", messages);
            requestInfo.put("messageCount", messages.size());
            requestInfo.put("requestToString", request.toString());

            String json = objectMapper.writeValueAsString(requestInfo);
            logger.debug("\n=== ChatClient Request ===\n{}\n=========================", json);
        } catch (Exception e) {
            logger.warn("요청 상세 정보를 로깅하는 데 실패했습니다.", e);
            logger.debug("요청 내용: {}", request);
        }
    }

    private Map<String, Object> formatMessage(Message message) {
        Map<String, Object> messageInfo = new HashMap<>();
        messageInfo.put("messageType", message.getMessageType().name());
        messageInfo.put("text", message.getText());
        return messageInfo;
    }

    private void logResponse(ChatClientResponse response) {
        try {
            Map<String, Object> responseInfo = new HashMap<>();

            Map<String, Object> metadataInfo = getMetadataInfo(response);
            responseInfo.put("metadata", metadataInfo);

            // QuestionAnswerAdvisor 검색된 문서 정보
            addRAGInfo(response, responseInfo);

            responseInfo.put("content", response.chatResponse().getResult().getOutput().getText());
            responseInfo.put("responseToString", response.toString());

            String json = objectMapper.writeValueAsString(responseInfo);
            logger.debug("\n=== ChatClient Response ===\n{}\n==========================", json);
        } catch (Exception e) {
            logger.warn("응답 상세 정보를 로깅하는 데 실패했습니다.", e);
            logger.debug("응답 내용: {}", response);
        }
    }

    private Map<String, Object> getMetadataInfo(ChatClientResponse response) {
        ChatResponseMetadata metadata = response.chatResponse().getMetadata();

        Usage usage = metadata.getUsage();

        Map<String, Object> usageInfo = new HashMap<>();
        usageInfo.put("promptTokens", usage.getPromptTokens());
        usageInfo.put("completionTokens", usage.getCompletionTokens());
        usageInfo.put("totalTokens", usage.getTotalTokens());

        Map<String, Object> metadataInfo = new HashMap<>();
        metadataInfo.put("usage", usageInfo);
        metadataInfo.put("model", metadata.getModel());

        return metadataInfo;
    }

    private void addRAGInfo(ChatClientResponse response, Map<String, Object> responseInfo) {
        Map<String, Object> context = response.context();
        Object retrievedDocsObj = context.get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);
        if (retrievedDocsObj instanceof List<?> retrievedDocs) {
            List<Map<String, Object>> docsInfo =
                    retrievedDocs.stream()
                            .filter(Document.class::isInstance)
                            .map(obj -> getMapFromDocument((Document) obj))
                            .toList();

            responseInfo.put("retrievedDocuments", docsInfo);
            responseInfo.put("retrievedDocumentsCount", docsInfo.size());
        }
    }

    private Map<String, Object> getMapFromDocument(Document doc) {
        Map<String, Object> docInfo = new HashMap<>();
        docInfo.put("id", doc.getId());
        docInfo.put("text", doc.getText());
        docInfo.put("metadata", doc.getMetadata());
        docInfo.put("score", doc.getScore());
        return docInfo;
    }
}
