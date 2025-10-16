package com.jnulocker.ai.adapter.in.docs;

import com.jnulocker.ai.application.port.in.request.UploadDocumentRequest;
import com.jnulocker.ai.application.port.in.response.DocumentCustomPage;
import com.jnulocker.ai.application.port.in.response.DocumentDetailResponse;
import com.jnulocker.ai.application.port.in.response.DocumentPageable;
import com.jnulocker.ai.application.port.in.response.UploadDocumentResponse;
import com.jnulocker.common.swagger.ApiExceptionExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "AI Document", description = "AI 문서 관리 API")
public interface DocumentApi {

    @ApiExceptionExamples(UploadDocumentExceptionDocs.class)
    @Operation(summary = "문서 업로드", description = "PDF/TXT 파일을 업로드하여 Vector Store에 저장합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "업로드 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UploadDocumentResponse.class)))
    ResponseEntity<UploadDocumentResponse> uploadDocument(
            @Parameter(description = "업로드할 파일 및 메타데이터") @Valid @ModelAttribute
                    UploadDocumentRequest request);

    @ApiExceptionExamples(GetDocumentsExceptionDocs.class)
    @Operation(summary = "문서 목록 조회", description = "업로드된 문서 목록을 페이징하여 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DocumentCustomPage.class)))
    ResponseEntity<DocumentCustomPage> getDocuments(
            @Parameter(description = "페이징 파라미터") @Valid @ParameterObject DocumentPageable pageable,
            @Parameter(description = "카테고리 필터 (선택)", example = "manual")
                    @RequestParam(required = false)
                    String category);

    @ApiExceptionExamples(GetDocumentExceptionDocs.class)
    @Operation(summary = "문서 상세 조회", description = "특정 문서의 상세 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DocumentDetailResponse.class)))
    ResponseEntity<DocumentDetailResponse> getDocument(
            @Parameter(description = "문서 ID", example = "1") @PathVariable("document-id")
                    Long documentId);

    @ApiExceptionExamples(DeleteDocumentExceptionDocs.class)
    @Operation(summary = "문서 삭제", description = "업로드한 문서를 삭제합니다. (본인만 삭제 가능)")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    ResponseEntity<Void> deleteDocument(
            @Parameter(description = "문서 ID", example = "1") @PathVariable("document-id")
                    Long documentId);
}
