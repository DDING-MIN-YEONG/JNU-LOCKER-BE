package com.jnulocker.ai.infrastructure.embedding;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.ai.huggingface")
@Getter
@Setter
public class HuggingFaceEmbeddingProperties {

    /** Hugging Face API 키 */
    private String apiKey;

    /** 사용할 임베딩 모델 (예: nlpai-lab/KURE-v1) */
    private String model;

    /** API 호출 타임아웃 (밀리초) */
    private int timeout; // 60초
}
