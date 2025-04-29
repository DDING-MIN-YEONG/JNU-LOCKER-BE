package com.jnulocker.registration.adapter.in.docs;

import com.jnulocker.common.swagger.ApiExceptionExamples;
import com.jnulocker.registration.application.port.in.request.RegisterForEventRequest;
import com.jnulocker.registration.application.port.in.response.RegistrationCustomPage;
import com.jnulocker.registration.application.port.in.response.RegistrationPageable;
import com.jnulocker.registration.application.port.in.response.RegistrationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "사물함 신청", description = "사물함 신청 관련 API")
public interface RegistrationApi {

    @ApiExceptionExamples(GetRegistrationsExceptionDocs.class)
    @Operation(summary = "사물함 신청 목록 조회", description = "이벤트에 대한 신청 목록을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "사물함 신청 목록 조회 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegistrationCustomPage.class)))
    ResponseEntity<RegistrationCustomPage> getRegistrations(
            @PathVariable("event-id") Long eventId,
            @Valid @ParameterObject RegistrationPageable registrationPageable);

    @ApiExceptionExamples(GetMyRegistrationExceptionDocs.class)
    @Operation(summary = "자신의 사물함 신청 현황 조회", description = "이벤트에 대한 자신의 신청 현황을 조회합니다")
    @ApiResponse(
            responseCode = "200",
            description = "자신의 사물함 신청 현황 조회 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegistrationResponse.class)))
    ResponseEntity<RegistrationResponse> getMyRegistration(@PathVariable("event-id") Long eventId);

    @ApiExceptionExamples(RegistrationForEventExceptionDocs.class)
    @Operation(summary = "사물함 신청", description = "이벤트에 신청합니다. 사물함을 선택할 수 있습니다.")
    @ApiResponse(responseCode = "201", description = "사물함 신청 성공")
    ResponseEntity<Void> registerForEvent(
            @PathVariable("event-id") Long eventId,
            @Valid @RequestBody RegisterForEventRequest request);
}
