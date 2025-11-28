package com.jnulocker.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.AbstractEmbeddingModel;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.lang.NonNull;

/** 테스트용 EmbeddingModel */
public class TestEmbeddingModel extends AbstractEmbeddingModel {

    private final int dimensions;

    public TestEmbeddingModel(int dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        List<String> instructions = request.getInstructions();
        List<Embedding> embeddings = new ArrayList<>(instructions.size());

        for (int i = 0; i < instructions.size(); i++) {
            float[] vector = generateDummyVector();
            embeddings.add(new Embedding(vector, i));
        }

        return new EmbeddingResponse(embeddings, createMetadata());
    }

    @Override
    public float[] embed(@NonNull Document document) {
        return generateDummyVector();
    }

    @Override
    public int dimensions() {
        return dimensions;
    }

    private float[] generateDummyVector() {
        float[] vector = new float[dimensions];
        Arrays.fill(vector, 0.0f);
        return vector;
    }

    private EmbeddingResponseMetadata createMetadata() {
        return new EmbeddingResponseMetadata("test-model", null);
    }
}
