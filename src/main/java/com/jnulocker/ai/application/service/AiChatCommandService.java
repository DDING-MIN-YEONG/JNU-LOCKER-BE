package com.jnulocker.ai.application.service;

import com.jnulocker.ai.application.port.in.AiChatCommand;
import com.jnulocker.ai.application.port.in.request.AiChatRequest;
import com.jnulocker.ai.application.port.in.response.AiChatResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChatCommandService implements AiChatCommand {

    private final ChatClient client;
    private final VectorStore vectorStore; // Elasticsearch Vector Store

    @Override
    public AiChatResponse chat(AiChatRequest request) {
        // Step 1: Retrieve relevant documents from Elasticsearch based on the user's query
        List<Document> relevantDocs = vectorStore.similaritySearch(request.message());
        StringBuilder context = new StringBuilder();
        for (Document doc : relevantDocs) {
            context.append(doc.getFormattedContent()).append("\n");
        }
        // Step 2: Construct the prompt with the retrieved documents as context
        String prompt =
                "Context:\n"
                        + context.toString()
                        + "\nUser Query: "
                        + request.message()
                        + "\nAnswer based on the context above.";
        // Step 3: Send the prompt to the ChatClient and get the response
        String message =
                client.prompt()
                        .system("You are a helpful assistant.")
                        .user(prompt)
                        .call()
                        .content();

        return AiChatResponse.of(message);
    }
}
