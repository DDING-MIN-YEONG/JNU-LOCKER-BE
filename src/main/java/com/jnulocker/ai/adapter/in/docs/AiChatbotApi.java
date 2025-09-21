package com.jnulocker.ai.adapter.in.docs;

import com.jnulocker.ai.application.port.in.request.AiChatRequest;
import com.jnulocker.ai.application.port.in.response.AiChatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "AI 챗봇", description = "AI 챗봇 관련 API")
public interface AiChatbotApi {

    @Operation(summary = "AI 채팅", description = "사용자 메시지를 입력받아 AI 응답을 반환합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AiChatResponse.class)))
    ResponseEntity<AiChatResponse> post(@Valid @RequestBody AiChatRequest request);
}
