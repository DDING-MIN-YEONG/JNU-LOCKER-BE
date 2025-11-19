package com.jnulocker.config;

import com.jnulocker.ai.infrastructure.embedding.HuggingFaceEmbeddingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class VectorStoreConfig {

    private final HuggingFaceEmbeddingModel embeddingModel;

    @Value("${spring.ai.vectorstore.pgvector.table-name}")
    private String tableName;

    @Value("${spring.ai.vectorstore.pgvector.dimensions}")
    private int dimensions;

    @Value("${spring.ai.vectorstore.pgvector.initialize-schema:true}")
    private boolean initializeSchema;

    @Bean
    public VectorStore vectorStore(JdbcTemplate jdbcTemplate) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName(tableName)
                .dimensions(dimensions)
                .initializeSchema(initializeSchema)
                .build();
    }
}
