package com.jnulocker.member.adapter.in.docs;

import com.jnulocker.common.swagger.ApiExceptionExamples;
import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 API", description = "회원 관련 API")
public interface MemberApi {

    @ApiExceptionExamples(GetMemberInfoExceptionDocs.class)
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "회원 정보 조회 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MemberInfoResponse.class)))
    ResponseEntity<MemberInfoResponse> getMemberInfo();
}
