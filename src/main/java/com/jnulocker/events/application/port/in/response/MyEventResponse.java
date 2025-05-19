package com.jnulocker.events.application.port.in.response;

import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record MyEventResponse(
        @Schema(description = "이벤트 ID", example = "2b0c4f8e-3d2a-4f5b-8c1e-6f7a2d3e4b5a") UUID id,
        @Schema(description = "이벤트 제목", example = "전자컴퓨터공학부 2025-2 사물함 신청") String title,
        @Schema(description = "이벤트 시작 시간", example = "2025-08-01T15:00:00") LocalDateTime startAt,
        @Schema(description = "이벤트 종료 시간", example = "2025-08-01T16:00:00") LocalDateTime endAt,
        @Schema(description = "이벤트 상태", example = "OPEN") EventStatus status) {

    public static MyEventResponse from(Event event) {
        return new MyEventResponse(
                event.getId(),
                event.getTitle(),
                event.getEventSchedule().getStartAt(),
                event.getEventSchedule().getEndAt(),
                event.getEventStatus());
    }
}
