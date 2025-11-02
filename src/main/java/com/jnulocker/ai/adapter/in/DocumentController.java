package com.jnulocker.ai.adapter.in;

import com.jnulocker.ai.adapter.in.docs.DocumentApi;
import com.jnulocker.ai.application.port.in.DocumentCommand;
import com.jnulocker.ai.application.port.in.DocumentQuery;
import com.jnulocker.ai.application.port.in.request.UploadDocumentRequest;
import com.jnulocker.ai.application.port.in.response.DocumentCustomPage;
import com.jnulocker.ai.application.port.in.response.DocumentDetailResponse;
import com.jnulocker.ai.application.port.in.response.DocumentPageable;
import com.jnulocker.ai.application.port.in.response.UploadDocumentResponse;
import com.jnulocker.auth.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai/documents")
public class DocumentController implements DocumentApi {

    private final DocumentCommand documentCommand;
    private final DocumentQuery documentQuery;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadDocumentResponse> uploadDocument(
            @Valid @ModelAttribute UploadDocumentRequest request) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        UploadDocumentResponse response = documentCommand.uploadDocument(request, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<DocumentCustomPage> getDocuments(
            @Valid @ParameterObject DocumentPageable pageable,
            @RequestParam(required = false) String category) {
        Pageable pageRequest = pageable.toPageable();
        return ResponseEntity.ok(documentQuery.getDocuments(pageRequest, category));
    }

    @Override
    @GetMapping("/{document-id}")
    public ResponseEntity<DocumentDetailResponse> getDocument(
            @PathVariable("document-id") Long documentId) {
        DocumentDetailResponse response = documentQuery.getDocument(documentId);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{document-id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("document-id") Long documentId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        documentCommand.deleteDocument(documentId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
