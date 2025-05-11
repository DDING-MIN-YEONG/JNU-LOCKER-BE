package com.jnulocker.announce.adapter.in.docs;

import com.jnulocker.announce.application.port.in.request.CreateAnnounceRequest;
import com.jnulocker.announce.application.port.in.response.AnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.AnnouncePageable;
import com.jnulocker.announce.application.port.in.response.MyAnnounceResponse;
import com.jnulocker.common.swagger.ApiExceptionExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "공지사항", description = "공지사항 관련 API")
public interface AnnounceApi {

    @ApiExceptionExamples(CreateAnnounceExceptionDocs.class)
    @Operation(summary = "공지사항 생성", description = "공지사항을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "공지사항 생성 성공")
    ResponseEntity<Void> createAnnounce(@Valid @RequestBody CreateAnnounceRequest request);

    @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 조회합니다. 페이지네이션이 지원됩니다.")
    @ApiResponse(
            responseCode = "200",
            description = "공지사항 목록 조회 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnnounceCustomPage.class)))
    ResponseEntity<AnnounceCustomPage> getAnnounces(
            @Valid @ParameterObject AnnouncePageable announcePageable);

    @ApiExceptionExamples(GetMyAnnouncesExceptionDocs.class)
    @Operation(summary = "자신의 소속학과 대상 공지사항 목록 조회", description = "자신의 소속학과가 참여한 공지사항 목록을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "자신의 소속학과 대상 공지사항 목록 조회 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array =
                                    @ArraySchema(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    MyAnnounceResponse.class))))
    ResponseEntity<List<MyAnnounceResponse>> getMyAnnounces();

    @ApiExceptionExamples(DeleteAnnounceExceptionDocs.class)
    @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "공지사항 삭제 성공")
    ResponseEntity<Void> deleteAnnounce(@PathVariable("announce-id") Long announceId);
}
