package com.jnulocker.registration.adapter.in.docs;

import com.jnulocker.common.swagger.ApiExceptionExamples;
import com.jnulocker.registration.application.port.in.request.RegisterForEventRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "사물함 신청 API", description = "사물함 신청 관련 API")
public interface RegistrationApi {

    @ApiExceptionExamples(RegistrationForEventExceptionDocs.class)
    @Operation(summary = "사물함 신청", description = "사물함 이벤트에 신청합니다. 사물함을 선택할 수 있습니다.")
    @ApiResponse(responseCode = "201", description = "사물함 신청 성공")
    ResponseEntity<Void> registerForEvent(
            @PathVariable("event-id") Long eventId,
            @Valid @RequestBody RegisterForEventRequest request);
}
