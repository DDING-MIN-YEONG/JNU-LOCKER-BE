package com.jnulocker.events.application.port.in.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record CreateEventRequest(
        @Schema(description = "이벤트 제목", example = "전자컴퓨터공학부 2025-2 사물함 신청")
                @NotBlank(message = "이벤트 제목은 필수입니다.")
                String title,
        @Schema(description = "이벤트 시작 시간", example = "2025-08-01T15:00:00")
                @NotNull(message = "이벤트 시작 시간은 필수입니다.")
                @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                LocalDateTime startAt,
        @Schema(description = "이벤트 종료 시간", example = "2025-08-01T16:00:00")
                @NotNull(message = "이벤트 종료 시간은 필수입니다.")
                @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                LocalDateTime endAt,
        @Schema(description = "참여 학과/학부 ID 목록", example = "[1, 2, 3]")
                @NotEmpty(message = "참여 학과/학부는 최소 1개 이상이어야 합니다.")
                List<Long> participationDepartmentIds,
        @Schema(description = "층 정보 목록") @NotEmpty(message = "층 정보는 최소 1개 이상이어야 합니다.") @Valid
                List<FloorInfo> floors) {

    @AssertTrue(message = "이벤트 종료 시간은 시작 시간 이후여야 합니다.")
    private boolean isEndAtAfterStartAt() {
        return startAt == null || endAt == null || !endAt.isBefore(startAt);
    }

    @AssertTrue(message = "전체 사물함 개수는 2000개를 초과할 수 없습니다.")
    private boolean isTotalLockersWithinLimit() {
        int lockerCount =
                floors.stream()
                        .flatMap(floor -> floor.prefixes().stream())
                        .flatMap(prefix -> prefix.ranges().stream())
                        .filter(
                                range ->
                                        range.lockerStartNumber() != null
                                                && range.lockerEndNumber() != null)
                        .mapToInt(range -> range.lockerEndNumber() - range.lockerStartNumber() + 1)
                        .sum();
        return lockerCount <= 2000;
    }
}
