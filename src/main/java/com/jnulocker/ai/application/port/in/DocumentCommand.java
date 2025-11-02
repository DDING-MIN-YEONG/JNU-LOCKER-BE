package com.jnulocker.ai.application.port.in;

import com.jnulocker.ai.application.port.in.request.UploadDocumentRequest;
import com.jnulocker.ai.application.port.in.response.UploadDocumentResponse;

public interface DocumentCommand {

    UploadDocumentResponse uploadDocument(UploadDocumentRequest request, Long memberId);

    void deleteDocument(Long documentId, Long memberId);
}
