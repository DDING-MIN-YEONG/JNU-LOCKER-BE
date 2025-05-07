package com.jnulocker.events.application.port.in.response;

import com.jnulocker.events.domain.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record MyEventResponse(
        @Schema(description = "이벤트 ID", example = "1") Long eventId,
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
