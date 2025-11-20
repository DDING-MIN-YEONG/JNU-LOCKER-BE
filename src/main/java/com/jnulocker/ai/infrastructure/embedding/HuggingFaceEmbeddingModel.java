package com.jnulocker.ai.infrastructure.embedding;

import com.jnulocker.ai.exception.EmbeddingException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.AbstractEmbeddingModel;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HuggingFaceEmbeddingModel extends AbstractEmbeddingModel {

    private final HuggingFaceEmbeddingClient client;
    private final HuggingFaceEmbeddingProperties properties;

    @Value("${spring.ai.vectorstore.pgvector.dimensions}")
    private int dimensions;

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        List<String> instructions = request.getInstructions();
        List<Embedding> embeddings = new ArrayList<>(instructions.size());

        for (int i = 0; i < instructions.size(); i++) {
            List<Double> vector = client.embed(instructions.get(i));
            float[] floatVector = convertToFloatArray(vector);
            embeddings.add(new Embedding(floatVector, i));
        }

        return new EmbeddingResponse(embeddings, createMetadata());
    }

    private EmbeddingResponseMetadata createMetadata() {
        return new EmbeddingResponseMetadata(properties.getModel(), null);
    }

    @Override
    public float[] embed(@NonNull Document document) {
        String text = document.getText();
        if (text == null || text.isBlank()) {
            throw EmbeddingException.INVALID_INPUT;
        }
        List<Double> vector = client.embed(text);
        return convertToFloatArray(vector);
    }

    private float[] convertToFloatArray(List<Double> vector) {
        float[] result = new float[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            Double value = vector.get(i);
            result[i] = value != null ? value.floatValue() : 0.0f;
        }
        return result;
    }

    @Override
    public int dimensions() {
        return dimensions;
    }
}
