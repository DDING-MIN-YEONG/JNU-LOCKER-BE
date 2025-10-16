package com.jnulocker.ai.application.service;

import com.jnulocker.ai.application.port.in.DocumentCommand;
import com.jnulocker.ai.application.port.in.DocumentQuery;
import com.jnulocker.ai.application.port.in.request.UploadDocumentRequest;
import com.jnulocker.ai.application.port.in.response.UploadDocumentResponse;
import com.jnulocker.ai.application.port.out.DocumentRecordPort;
import com.jnulocker.ai.domain.AiDocument;
import com.jnulocker.ai.exception.UnauthorizedDocumentAccessException;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DocumentCommandService implements DocumentCommand {

    private final DocumentRecordPort documentRecordPort;
    private final DocumentQuery documentQuery;
    private final MemberQuery memberQuery;
    private final VectorStoreManager vectorStoreManager;
    private final DocumentFileHandler documentFileHandler;
    private final VectorStoreIdSerializer vectorStoreIdSerializer;

    @Override
    public UploadDocumentResponse uploadDocument(UploadDocumentRequest request, Long memberId) {
        Member member = memberQuery.findByIdOrThrow(memberId);

        MultipartFile file = request.file();
        documentFileHandler.validateFile(file);

        List<Document> chunks = documentFileHandler.parseDocument(file);

        // 메타데이터 추가
        String originalFileName = file.getOriginalFilename();
        chunks.forEach(
                chunk -> {
                    chunk.getMetadata().put("fileName", originalFileName);
                    chunk.getMetadata().put("category", request.category());
                });

        // Vector Store 저장
        List<String> vectorIds = vectorStoreManager.addDocuments(chunks);

        // DB에 메타데이터 저장
        String fileType = documentFileHandler.getFileType(file);
        String fileName = documentFileHandler.generateFileName(originalFileName, fileType);

        AiDocument document =
                AiDocument.create(
                        fileName,
                        originalFileName,
                        file.getSize(),
                        fileType,
                        request.category(),
                        chunks.size(),
                        member,
                        vectorStoreIdSerializer.toJson(vectorIds));

        AiDocument savedDocument = documentRecordPort.save(document);

        log.info(
                "문서 업로드 성공: id={}, fileName={}, chunkCount={}",
                savedDocument.getId(),
                savedDocument.getFileName(),
                savedDocument.getChunkCount());

        return UploadDocumentResponse.from(savedDocument);
    }

    @Override
    public void deleteDocument(Long documentId, Long memberId) {
        AiDocument document = documentQuery.getDocumentEntity(documentId);

        if (!document.getUploadedBy().getId().equals(memberId)) {
            throw UnauthorizedDocumentAccessException.EXCEPTION;
        }

        // Vector Store에서 삭제
        List<String> vectorIds =
                vectorStoreIdSerializer.parseVectorStoreIds(document.getVectorStoreIds());
        vectorStoreManager.deleteDocuments(vectorIds);

        // DB에서 삭제
        documentRecordPort.deleteById(documentId);

        log.info("문서 삭제 성공: id={}, fileName={}", documentId, document.getFileName());
    }
}
