package com.jnulocker.announce.adapter.in.docs;

import com.jnulocker.announce.application.port.in.request.CreateAnnounceRequest;
import com.jnulocker.common.swagger.ApiExceptionExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "공지사항", description = "공지사항 관련 API")
public interface AnnounceApi {

    @ApiExceptionExamples(CreateAnnounceExceptionDocs.class)
    @Operation(summary = "공지사항 생성", description = "공지사항을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "공지사항 생성 성공")
    ResponseEntity<Void> createAnnounce(@Valid @RequestBody CreateAnnounceRequest request);
}
