package com.jnulocker.events.adapter.in.docs;

import com.jnulocker.common.swagger.ApiExceptionExamples;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "사물함 신청 이벤트", description = "사물함 신청 이벤트 관련 API")
public interface EventApi {

    @ApiExceptionExamples(GetLockerExceptionDocs.class)
    @Operation(summary = "사물함 목록 조회", description = "사물함 신청 이벤트에 대한 사물함 목록을 조회합니다.")
    ResponseEntity<List<FloorWithLockersResponse>> getLockers(
            @PathVariable("event-id") Long eventId);
}
