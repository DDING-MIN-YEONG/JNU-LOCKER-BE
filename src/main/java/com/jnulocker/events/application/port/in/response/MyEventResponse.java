package com.jnulocker.events.application.port.in.response;

import com.jnulocker.events.domain.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record MyEventResponse(
        @Schema(description = "이벤트 ID", example = "2b0c4f8e-3d2a-4f5b-8c1e-6f7a2d3e4b5a") UUID id,
        @Schema(description = "이벤트 제목", example = "전자컴퓨터공학부 2025-2 사물함 신청") String title,
        @Schema(description = "이벤트 주최 조직이름", example = "전자컴퓨터공학부 학생회") String departmentNickname,
        @Schema(description = "이벤트 시작 시간", example = "2025-08-01T15:00:00") LocalDateTime startAt,
        @Schema(description = "이벤트 종료 시간", example = "2025-08-01T16:00:00") LocalDateTime endAt,
        @Schema(description = "신청 가능한 사물함 개수", example = "5") Integer availableLockerCount) {

    public static MyEventResponse of(Event event, Integer availableLockerCount) {
        return new MyEventResponse(
                event.getId(),
                event.getTitle(),
                event.getDepartment().getNickname(),
                event.getEventSchedule().getStartAt(),
                event.getEventSchedule().getEndAt(),
                availableLockerCount);
    }
}
