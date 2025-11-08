package com.jnulocker.ai.utils;

import com.jnulocker.ai.adapter.out.AiDocumentJpaRepository;
import com.jnulocker.ai.application.service.VectorStoreIdSerializer;
import com.jnulocker.ai.application.service.VectorStoreManager;
import com.jnulocker.ai.domain.AiDocument;
import com.jnulocker.member.domain.Member;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

@Component
public class DocumentTestUtil {

    @Autowired private AiDocumentJpaRepository aiDocumentJpaRepository;
    @Autowired private VectorStoreManager vectorStoreManager;
    @Autowired private VectorStoreIdSerializer vectorStoreIdSerializer;

    /** PDF 형식의 Mock 파일 생성 (최소한의 유효한 PDF) */
    public MockMultipartFile createMockPdfFile(String fileName) {
        // 최소한의 유효한 PDF 바이트 배열 생성
        String pdfContent =
                """
                %PDF-1.4
                1 0 obj
                <<
                /Type /Catalog
                /Pages 2 0 R
                >>
                endobj
                2 0 obj
                <<
                /Type /Pages
                /Kids [3 0 R]
                /Count 1
                >>
                endobj
                3 0 obj
                <<
                /Type /Page
                /Parent 2 0 R
                /Resources <<
                /Font <<
                /F1 <<
                /Type /Font
                /Subtype /Type1
                /BaseFont /Helvetica
                >>
                >>
                >>
                /MediaBox [0 0 612 792]
                /Contents 4 0 R
                >>
                endobj
                4 0 obj
                <<
                /Length 44
                >>
                stream
                BT
                /F1 12 Tf
                100 700 Td
                (Test PDF Document) Tj
                ET
                endstream
                endobj
                xref
                0 5
                0000000000 65535 f\s
                0000000009 00000 n\s
                0000000056 00000 n\s
                0000000115 00000 n\s
                0000000344 00000 n\s
                trailer
                <<
                /Size 5
                /Root 1 0 R
                >>
                startxref
                437
                %%EOF""";
        return new MockMultipartFile("file", fileName, "application/pdf", pdfContent.getBytes());
    }

    /** TXT 형식의 Mock 파일 생성 */
    public MockMultipartFile createMockTxtFile(String fileName) {
        return new MockMultipartFile(
                "file", fileName, "text/plain", "Text content for testing".getBytes());
    }

    /** 지원하지 않는 형식의 Mock 파일 생성 (예: JPG) */
    public MockMultipartFile createMockUnsupportedFile(String fileName) {
        return new MockMultipartFile(
                "file", fileName, "image/jpeg", "Unsupported content".getBytes());
    }

    /** 빈 Mock 파일 생성 */
    public MockMultipartFile createEmptyMockFile(String fileName) {
        return new MockMultipartFile("file", fileName, "application/pdf", new byte[0]);
    }

    /** 테스트용 AiDocument 엔티티 생성 및 저장 */
    public AiDocument createDocument(Member member, String category) {
        String vectorId1 = UUID.randomUUID().toString();
        String vectorId2 = UUID.randomUUID().toString();
        String vectorStoreIds = String.format("[\"%s\", \"%s\"]", vectorId1, vectorId2);

        AiDocument document =
                AiDocument.create(
                        "test-document.pdf",
                        "원본파일.pdf",
                        1024L,
                        "PDF",
                        category,
                        5,
                        member,
                        vectorStoreIds);

        return aiDocumentJpaRepository.save(document);
    }

    /** 여러 개의 테스트용 문서 생성 */
    public void createMultipleDocuments(Member member, int count, String category) {
        for (int i = 0; i < count; i++) {
            String vectorId1 = UUID.randomUUID().toString();
            String vectorId2 = UUID.randomUUID().toString();
            String vectorStoreIds = String.format("[\"%s\", \"%s\"]", vectorId1, vectorId2);

            AiDocument document =
                    AiDocument.create(
                            "test-document-" + i + ".pdf",
                            "원본파일-" + i + ".pdf",
                            1024L * (i + 1),
                            "PDF",
                            category,
                            5 + i,
                            member,
                            vectorStoreIds);
            aiDocumentJpaRepository.save(document);
        }
    }

    /** ElasticSearch에서 VectorStore 데이터 삭제 */
    public void deleteAllVectorStoreData() {
        try {
            List<AiDocument> documents = aiDocumentJpaRepository.findAll();
            for (AiDocument document : documents) {
                String vectorStoreIdsJson = document.getVectorStoreIds();
                List<String> vectorIds =
                        vectorStoreIdSerializer.parseVectorStoreIds(vectorStoreIdsJson);
                if (!vectorIds.isEmpty()) {
                    vectorStoreManager.deleteDocuments(vectorIds);
                }
            }
        } catch (Exception e) {
            // 테스트 정리 중 실패는 무시 (다음 테스트를 위해 계속 진행)
            System.err.println(
                    "Failed to delete vector store data during test cleanup: " + e.getMessage());
        }
    }

    /** 모든 문서 삭제 */
    public void deleteAll() {
        aiDocumentJpaRepository.deleteAll();
    }
}
