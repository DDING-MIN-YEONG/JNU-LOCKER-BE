package com.jnulocker.events.application.port.in.response;

import com.jnulocker.events.domain.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record EventListItem(
        @Schema(description = "이벤트 ID", example = "1") Long id,
        @Schema(description = "이벤트 제목", example = "전자컴퓨터공학부 2025-2 사물함 신청") String title,
        @Schema(description = "이벤트 시작 시간", example = "2025-08-01T15:00:00") LocalDateTime startAt,
        @Schema(description = "이벤트 종료 시간", example = "2025-08-01T16:00:00") LocalDateTime endAt,
        @Schema(description = "이벤트 게시 여부", example = "true") Boolean publish) {

    public static EventListItem from(Event event) {
        return new EventListItem(
                event.getId(),
                event.getTitle(),
                event.getEventSchedule().getStartAt(),
                event.getEventSchedule().getEndAt(),
                event.getPublish());
    }
}
