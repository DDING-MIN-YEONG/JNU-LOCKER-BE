package com.jnulocker.events.adapter.in.docs;

import com.jnulocker.common.swagger.ApiExceptionExamples;
import com.jnulocker.events.application.port.in.request.CreateEventRequest;
import com.jnulocker.events.application.port.in.request.PublishEventRequest;
import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.EventPageable;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
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

@Tag(name = "이벤트", description = "이벤트 관련 API")
public interface EventApi {

    @Operation(summary = "이벤트 목록 조회", description = "이벤트 목록을 조회합니다. 페이지네이션이 지원됩니다.")
    @ApiResponse(
            responseCode = "200",
            description = "이벤트 목록 조회 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EventCustomPage.class)))
    ResponseEntity<EventCustomPage> getEvents(@Valid @ParameterObject EventPageable eventPageable);

    @ApiExceptionExamples(CreateEventExceptionDocs.class)
    @Operation(
            summary = "이벤트 생성",
            description = "새로운 이벤트를 생성합니다. 이벤트 상태는 기본적으로 READY, 게시 상태는 false로 설정됩니다.")
    @ApiResponse(responseCode = "201", description = "이벤트 생성 성공")
    ResponseEntity<Void> createEvent(@Valid @RequestBody CreateEventRequest request);

    @ApiExceptionExamples(GetLockerExceptionDocs.class)
    @Operation(summary = "사물함 목록 조회", description = "이벤트에 대한 사물함 목록을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "사물함 목록 조회 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array =
                                    @ArraySchema(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    FloorWithLockersResponse
                                                                            .class))))
    ResponseEntity<List<FloorWithLockersResponse>> getLockers(
            @PathVariable("event-id") Long eventId);

    @ApiExceptionExamples(DeleteEventExceptionDocs.class)
    @Operation(summary = "이벤트 삭제", description = "이벤트를 삭제합니다. 이벤트가 진행 중인 경우에는 삭제할 수 없습니다.")
    @ApiResponse(responseCode = "204", description = "이벤트 삭제 성공")
    ResponseEntity<Void> deleteEvent(@PathVariable("event-id") Long eventId);

    @Operation(summary = "이벤트 publish", description = "이벤트를 publish 또는 unpublish합니다.")
    @ApiExceptionExamples(PublishEventExceptionDocs.class)
    ResponseEntity<Void> publishEvent(
            @PathVariable("event-id") Long eventId,
            @RequestBody @Valid PublishEventRequest request);
}
