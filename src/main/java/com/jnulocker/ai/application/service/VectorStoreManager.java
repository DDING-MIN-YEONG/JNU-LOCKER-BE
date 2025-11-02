package com.jnulocker.ai.application.service;

import com.jnulocker.ai.exception.VectorStoreException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VectorStoreManager {

    private final VectorStore vectorStore;

    /**
     * 문서 청크들을 Vector Store에 저장하고 생성된 Vector ID 목록을 반환
     *
     * @param chunks 저장할 문서 청크 목록
     * @return 생성된 Vector ID 목록
     * @throws VectorStoreException Vector Store 저장 실패 시
     */
    public List<String> addDocuments(List<Document> chunks) {
        try {
            vectorStore.add(chunks);
            List<String> vectorIds = chunks.stream().map(Document::getId).toList();
            log.info("Vector Store 저장 성공: 청크 수 = {}, Vector IDs = {}", chunks.size(), vectorIds);
            return vectorIds;
        } catch (Exception e) {
            log.error("Vector Store 저장 실패: {}", e.getMessage(), e);
            throw VectorStoreException.SAVE_FAILED;
        }
    }

    /**
     * Vector Store에서 지정된 Vector ID 목록의 문서들을 삭제
     *
     * @param vectorIds 삭제할 Vector ID 목록
     * @throws VectorStoreException Vector Store 삭제 실패 시
     */
    public void deleteDocuments(List<String> vectorIds) {
        if (vectorIds == null || vectorIds.isEmpty()) {
            log.debug("No vector IDs to delete");
            return;
        }

        try {
            vectorStore.delete(vectorIds);
            log.info("Vector Store deleted successfully: {} vector IDs", vectorIds.size());
        } catch (Exception e) {
            log.error("Failed to delete documents from Vector Store: {}", e.getMessage(), e);
            throw VectorStoreException.DELETE_FAILED;
        }
    }
}
