package com.jnulocker.ai.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VectorStoreIdSerializer {

    private final ObjectMapper objectMapper;

    public String toJson(List<String> vectorIds) {
        // null이나 빈 리스트는 명시적으로 빈 JSON 배열로 반환
        if (vectorIds == null || vectorIds.isEmpty()) {
            return "[]";
        }

        try {
            return objectMapper.writeValueAsString(vectorIds);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize vectorIds: {}", e.getMessage(), e);
            throw new IllegalStateException("Vector Store ID 직렬화에 실패했습니다.", e);
        }
    }

    public List<String> parseVectorStoreIds(String vectorStoreIds) {
        if (vectorStoreIds == null || vectorStoreIds.isBlank()) {
            return List.of();
        }

        try {
            return objectMapper.readValue(vectorStoreIds, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("Failed to parse vectorStoreIds: {}", e.getMessage(), e);
            return List.of();
        }
    }
}
