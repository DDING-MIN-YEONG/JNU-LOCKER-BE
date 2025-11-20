package com.jnulocker.ai.infrastructure.embedding;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.ai.exception.EmbeddingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class HuggingFaceEmbeddingClient {
    private static final String HF_INFERENCE_URL =
            "https://router.huggingface.co/hf-inference/models/{model}/pipeline/feature-extraction";
    private static final TypeReference<List<Double>> EMBEDDING_TYPE_REF = new TypeReference<>() {};
    private static final int MAX_ERROR_BODY_SIZE = 8192; // 8KB

    private final HuggingFaceEmbeddingProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient huggingFaceRestClient;

    public List<Double> embed(String text) {
        validateInput(text);

        try {
            String responseBody =
                    huggingFaceRestClient
                            .post()
                            .uri(HF_INFERENCE_URL, properties.getModel())
                            .body(createRequestBody(text))
                            .retrieve()
                            .onStatus(HttpStatusCode::is4xxClientError, handleError(true))
                            .onStatus(HttpStatusCode::is5xxServerError, handleError(false))
                            .body(String.class);

            return parseAndValidateResponse(responseBody);

        } catch (EmbeddingException e) {
            throw e;
        } catch (Exception e) {
            log.error("임베딩 생성 중 예상치 못한 오류 발생", e);
            throw EmbeddingException.GENERATION_FAILED;
        }
    }

    private void validateInput(String text) {
        if (text == null || text.isBlank()) {
            throw EmbeddingException.INVALID_INPUT;
        }
    }

    private Map<String, String> createRequestBody(String text) {
        return Map.of("inputs", text);
    }

    private List<Double> parseAndValidateResponse(String responseBody) {
        try {
            List<Double> embedding = objectMapper.readValue(responseBody, EMBEDDING_TYPE_REF);

            if (embedding == null || embedding.isEmpty()) {
                throw EmbeddingException.EMPTY_RESPONSE;
            }

            return embedding;
        } catch (EmbeddingException e) {
            throw e;
        } catch (Exception e) {
            log.error("임베딩 응답 파싱 실패: {}", responseBody, e);
            throw EmbeddingException.GENERATION_FAILED;
        }
    }

    private ErrorHandler handleError(boolean isClientError) {
        return (request, response) -> {
            String errorBody = readErrorBody(response);

            log.error(
                    "Hugging Face 임베딩 API 오류 응답: 상태 코드={}, 본문={}, 클라이언트 오류={}",
                    response.getStatusCode(),
                    errorBody,
                    isClientError);

            throw isClientError
                    ? EmbeddingException.API_CLIENT_ERROR
                    : EmbeddingException.API_SERVER_ERROR;
        };
    }

    private String readErrorBody(org.springframework.http.client.ClientHttpResponse response) {
        try (var inputStream = response.getBody()) {
            byte[] buffer = new byte[MAX_ERROR_BODY_SIZE];
            int bytesRead = inputStream.read(buffer);
            return bytesRead > 0
                    ? new String(buffer, 0, bytesRead, StandardCharsets.UTF_8)
                    : "응답 본문 없음";
        } catch (Exception e) {
            log.warn("오류 응답 본문 읽기 실패", e);
            return "오류 응답 읽기 실패";
        }
    }
}
